package africa.springCore.martbackend.portfolio.admin.service;

import africa.springCore.martbackend.core.domain.dtos.response.ApiResponse;
import africa.springCore.martbackend.core.domain.enums.Role;
import africa.springCore.martbackend.core.utils.JwtUtility;
import africa.springCore.martbackend.core.domain.dtos.response.BioDataResponseDto;
import africa.springCore.martbackend.core.domain.model.BioData;
import africa.springCore.martbackend.core.domain.repository.BioDataRepository;
import africa.springCore.martbackend.core.service.BioDataService;
import africa.springCore.martbackend.core.portfolio.admin.domain.dtos.requests.AdminInvitationRequest;
import africa.springCore.martbackend.core.portfolio.admin.exception.AdminNotFoundException;
import africa.springCore.martbackend.core.portfolio.admin.exception.AdminUpdateFailedException;
import africa.springCore.martbackend.core.portfolio.customer.exception.CustomerCreationFailedException;
import africa.springCore.martbackend.infrastructure.configuration.ApplicationProperty;
import africa.springCore.martbackend.infrastructure.exception.*;
import africa.springCore.martbackend.core.utils.MartMapper;
import africa.springCore.martbackend.infrastructure.notification.mailServices.domain.data.Recipient;
import africa.springCore.martbackend.infrastructure.notification.mailServices.domain.dtos.EmailNotificationRequest;
import africa.springCore.martbackend.infrastructure.notification.mailServices.service.MailService;
import africa.springCore.martbackend.portfolio.admin.domain.dtos.requests.AdminUpdateRequest;
import africa.springCore.martbackend.portfolio.admin.domain.dtos.responses.AdminDashboardResponse;
import africa.springCore.martbackend.portfolio.admin.domain.dtos.responses.AdminListingDto;
import africa.springCore.martbackend.portfolio.admin.domain.dtos.responses.AdminResponseDto;
import africa.springCore.martbackend.portfolio.admin.domain.model.Admin;
import africa.springCore.martbackend.portfolio.admin.domain.repository.AdminRepository;
import africa.springCore.martbackend.portfolio.customer.service.CustomerService;
import africa.springCore.martbackend.portfolio.dispatchRider.service.DispatchRiderService;
import africa.springCore.martbackend.portfolio.order.service.OrderService;
import africa.springCore.martbackend.portfolio.product.domain.dtos.response.CategoryProductCountDto;
import africa.springCore.martbackend.portfolio.product.domain.model.ProductCategory;
import africa.springCore.martbackend.portfolio.product.domain.repository.ProductCategoryRepository;
import africa.springCore.martbackend.portfolio.product.domain.repository.ProductRepository;
import africa.springCore.martbackend.portfolio.vendor.service.VendorService;
import com.auth0.jwt.interfaces.Claim;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static africa.springCore.martbackend.core.utils.AppUtils.*;
import static africa.springCore.martbackend.core.utils.Message.*;
import static africa.springCore.martbackend.core.utils.HtmlFileUtility.getFileTemplateFromClasspath;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final MartMapper martMapper;
    private final AdminRepository adminRepository;
    private final JwtUtility jwtUtility;
    private final ApplicationProperty applicationProperty;
    private final MailService mailService;
    private final BioDataService bioDataService;
    private final DispatchRiderService dispatchRiderService;
    private final BioDataRepository bioDataRepository;
    private final OrderService orderService;
    private final PasswordEncoder passwordEncoder;
    private final CustomerService customerService;
    private final ProductRepository productRepository;
    private final VendorService vendorService;
    private final ProductCategoryRepository productCategoryRepository;


    @Override
    public ApiResponse sendInvitationLink(String emailAddress) throws MartException {
        validateDuplicateUserExistence(emailAddress);
        Map<String, Object> requestAsMap = buildRequestAsMap(emailAddress);
        String encryptedEmail = jwtUtility.generateEncryptedLink(requestAsMap);
        String invitationLink = applicationProperty.getAdminInvitationClientUrl().concat(encryptedEmail);
        Recipient recipient = Recipient.builder().email(emailAddress).build();
        EmailNotificationRequest emailRequest = new EmailNotificationRequest();
        emailRequest.setTo(List.of(recipient));
        emailRequest.setSubject(MART_ADMIN_INVITATION);
        String template = getFileTemplateFromClasspath(ADMIN_INVITATION_HTML_TEMPLATE_LOCATION);
        System.out.println("Template retrieved");
        emailRequest.setText(String.format(template, invitationLink));
        try {
            mailService.sendHtmlMail(emailRequest);
        } catch (Exception e) {
            log.info("Admin Invitation {}", e.getMessage());
        }
        return apiResponse(MAIL_HAS_BEEN_SENT_SUCCESSFULLY);
    }


    private Map<String, Object> buildRequestAsMap(String emailAddress) {
        Map<String, Object> map = new HashMap<>();
        map.put(EMAIL_VALUE, emailAddress);
        return map;
    }

    @Override
    public ApiResponse acceptInvitation(String encryptedLink, AdminInvitationRequest request) throws MartException {
        Admin admin = getAdmin(encryptedLink, request);
        if (adminRepository.findByBioData_EmailAddress(admin.getBioData().getEmailAddress()).isPresent()) {
            throw new UserAlreadyExistsException("You have already registered on this platform");
        }
        String randomPassword = jwtUtility.generateAdminDefaultPassword(admin.getBioData().getEmailAddress());
        String encryptedPassword = passwordEncoder.encode(randomPassword);
        admin.getBioData().setPassword(encryptedPassword);
        admin.getBioData().setRoles(List.of(Role.ORDINARY_ADMIN));
        adminRepository.save(admin);
        String template = getFileTemplateFromClasspath(SUCCESSFUL_REGISTRATION_HTML_TEMPLATE_LOCATION);
        template = String.format(template, admin.getBioData().getEmailAddress(), randomPassword);

        Recipient recipient = Recipient.builder().email(admin.getBioData().getEmailAddress()).build();
        EmailNotificationRequest emailRequest = new EmailNotificationRequest();
        emailRequest.setTo(List.of(recipient));
        emailRequest.setSubject(MART_ADMIN_INVITATION);
        emailRequest.setText(template);
        try {
            mailService.sendHtmlMail(emailRequest);
        } catch (Exception e) {
            log.info("Admin Invitation {}", e.getMessage());
        }
        return apiResponse("Registration is successful. Please, check your mail.");
    }

    @Override
    public AdminDashboardResponse retrieveAdminDashboard(Pageable pageable) {
        List<Object[]> results = productRepository.findTopByProductCount(pageable.getPageSize(), pageable.getOffset());
        return AdminDashboardResponse.builder()
                .totalOrders(orderService.retrieveTotalOrders())
                .totalCustomers(customerService.retrieveTotalCustomers())
                .totalProducts(productRepository.count())
                .totalTransporters(dispatchRiderService.retrieveTotalTransporters())
                .totalVendors(vendorService.retrieveTotalVendors())
                .categoryProductCount(
                        results.stream()
                                .map(result -> {
                                    Long categoryId = (Long) result[0];
                                    Long productCount = (Long) result[1];
                                    ProductCategory category = productCategoryRepository.findById(categoryId)
                                            .orElseThrow(() -> new RuntimeException("Category not found"));
                                    return new CategoryProductCountDto(category, productCount);
                                })
                                .toList()
                )
                .build();
    }

    @Override
    public ApiResponse validateToken(String token) {
        jwtUtility.validateToken(token);
        return apiResponse(TOKEN_HAS_BEEN_VERIFIED_SUCCESSFULLY);
    }

    @Override
    public AdminListingDto findAll(Pageable pageable) {
        return getAdminListingDto(
                adminRepository.findAll(pageable)
        );
    }

    @Override
    public AdminResponseDto findById(Long id) throws AdminNotFoundException, MapperException {
        return getAdminResponseDto(
                adminRepository.findById(id).orElseThrow(
                        () -> new AdminNotFoundException(String.format(ADMIN_WITH_ID_NOT_FOUND, id))
                )
        );
    }

    @Override
    public AdminResponseDto updateAdmin(Long id, AdminUpdateRequest adminUpdateRequest) throws AdminNotFoundException, MapperException, AdminUpdateFailedException, UserNotFoundException, CustomerCreationFailedException {
        boolean allFieldsAreEmpty = true;
        findById(id);
        Admin existingAdmin = adminRepository.findById(id).get();
        BioData existingAdminBioData = existingAdmin.getBioData();
        if (!StringUtils.isBlank(adminUpdateRequest.getEmailAddress()) && !adminUpdateRequest.getEmailAddress().equalsIgnoreCase(existingAdminBioData.getEmailAddress())) {
            allFieldsAreEmpty = false;
            validateEmailDuplicity(adminUpdateRequest.getEmailAddress());
            existingAdminBioData.setEmailAddress(adminUpdateRequest.getEmailAddress());
        }
        if (!StringUtils.isBlank(adminUpdateRequest.getPhoneNumber()) && !adminUpdateRequest.getPhoneNumber().equalsIgnoreCase(existingAdminBioData.getPhoneNumber())) {
            allFieldsAreEmpty = false;
            validatePhoneNumberDuplicity(adminUpdateRequest.getPhoneNumber());
            existingAdminBioData.setPhoneNumber(adminUpdateRequest.getPhoneNumber());
        }
        if (adminUpdateRequest.getFirstName() != null && !StringUtils.isBlank(adminUpdateRequest.getFirstName())) {
            allFieldsAreEmpty = false;
            existingAdminBioData.setFirstName(adminUpdateRequest.getFirstName());
        }
        if (adminUpdateRequest.getLastName() != null && !StringUtils.isBlank(adminUpdateRequest.getLastName())) {
            allFieldsAreEmpty = false;
            existingAdminBioData.setLastName(adminUpdateRequest.getLastName());
        }

        if (allFieldsAreEmpty) throw new AdminUpdateFailedException("No field specified for update");
        else {
            existingAdmin.setBioData(existingAdminBioData);
            return getAdminResponseDto(adminRepository.save(existingAdmin));
        }
    }


    private void validateEmailDuplicity(String emailAddress) throws CustomerCreationFailedException, UserNotFoundException, MapperException {
        if (emailAddress == null || StringUtils.isEmpty(emailAddress)) {
            throw new CustomerCreationFailedException("Validation failed, emailAddress cannot be null");
        }
        Optional<Admin> foundAdmin = adminRepository.findByBioData_EmailAddress(emailAddress);
        Optional<BioData> foundUser = bioDataRepository.findByEmailAddress(emailAddress);
        if (foundAdmin.isPresent()) {
            throw new CustomerCreationFailedException(
                    String.format(ADMIN_WITH_EMAIL_ALREADY_EXISTS, emailAddress)
            );
        }
        if (foundUser.isPresent()) {
            throw new CustomerCreationFailedException(
                    String.format(USER_WITH_EMAIL_ALREADY_EXISTS, emailAddress)
            );
        }
    }

    private void validatePhoneNumberDuplicity(String phoneNumber) throws CustomerCreationFailedException, UserNotFoundException, MapperException {
        if (phoneNumber == null || StringUtils.isEmpty(phoneNumber)) {
            throw new CustomerCreationFailedException("Validation failed, emailAddress cannot be null");
        }
        Optional<Admin> foundAdmin = adminRepository.findByBioData_PhoneNumber(phoneNumber);
        Optional<BioData> foundUser = bioDataRepository.findByPhoneNumber(phoneNumber);
        if (foundAdmin.isPresent()) {
            throw new CustomerCreationFailedException(
                    String.format(ADMIN_WITH_PHONE_NUMBER_ALREADY_EXISTS, phoneNumber)
            );
        }
        if (foundUser.isPresent()) {
            throw new CustomerCreationFailedException(
                    String.format(USER_WITH_PHONE_NUMBER_ALREADY_EXISTS, phoneNumber)
            );
        }
    }


    private AdminListingDto getAdminListingDto(Page<Admin> admins) {
        Page<AdminResponseDto> responseDtoPage = admins.map(
                admin -> {
                    try {
                        return getAdminResponseDto(admin);
                    } catch (MapperException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
        );
        AdminListingDto adminListingDto = new AdminListingDto();
        adminListingDto.setAdmins(responseDtoPage.getContent());
        adminListingDto.setPageNumber(responseDtoPage.getNumber());
        adminListingDto.setPageSize(responseDtoPage.getSize());
        return adminListingDto;
    }

    private AdminResponseDto getAdminResponseDto(Admin admin) throws MapperException {
        return martMapper.readValue(admin, AdminResponseDto.class);
    }

    private Admin getAdmin(String encryptedLink, AdminInvitationRequest request) throws AuthenticationException, MapperException {
        Claim claim = jwtUtility.extractClaimFrom(encryptedLink, ADMIN);
        Map<String, Object> adminAsMap = claim.asMap();
        BioData bioData = martMapper.readValue(request, BioData.class);
        bioData.setEmailAddress((String) adminAsMap.get(EMAIL_VALUE));
        Admin admin = new Admin();
        admin.setBioData(bioData);
        return admin;
    }


    private void validateDuplicateUserExistence(String emailAddress) throws UserAlreadyExistsException {
        bioDataService.validateDuplicateUserExistence(emailAddress);
    }


    @Override
    public AdminResponseDto findByEmail(String emailAddress) throws MapperException, UserNotFoundException {
        Admin foundAdmin = adminRepository.findByBioData_EmailAddress(emailAddress).orElseThrow(
                () -> new UserNotFoundException(String.format(USER_WITH_EMAIL_NOT_FOUND, emailAddress))
        );
        BioDataResponseDto bioDataResponse = martMapper.readValue(foundAdmin.getBioData(), BioDataResponseDto.class);
        AdminResponseDto adminResponseDto = martMapper.readValue(foundAdmin, AdminResponseDto.class);
        adminResponseDto.setBioData(bioDataResponse);
        return adminResponseDto;
    }
}
