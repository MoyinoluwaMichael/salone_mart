package africa.springCore.martbackend.portfolio.customer.service;

import africa.springCore.martbackend.core.base.domain.dtos.response.BioDataResponseDto;
import africa.springCore.martbackend.core.base.domain.model.MediaType;
import africa.springCore.martbackend.core.base.domain.repository.BioDataRepository;
import africa.springCore.martbackend.core.portfolio.customer.domain.dtos.requests.CustomerCreationRequest;
import africa.springCore.martbackend.core.portfolio.customer.domain.dtos.requests.CustomerUpdateRequest;
import africa.springCore.martbackend.core.portfolio.customer.exception.CustomerCreationFailedException;
import africa.springCore.martbackend.core.base.domain.model.BioData;
import africa.springCore.martbackend.common.enums.Role;
import africa.springCore.martbackend.common.utils.MartMapper;
import africa.springCore.martbackend.infrastructure.cloudservice.storageservice.service.CloudinaryUploadService;
import africa.springCore.martbackend.infrastructure.exception.MartException;
import africa.springCore.martbackend.infrastructure.exception.MapperException;
import africa.springCore.martbackend.infrastructure.exception.UserNotFoundException;
import africa.springCore.martbackend.infrastructure.exception.UserUpdateFailedException;
import africa.springCore.martbackend.portfolio.customer.domain.dtos.responses.CustomerListingDto;
import africa.springCore.martbackend.portfolio.customer.domain.dtos.responses.CustomerResponseDto;
import africa.springCore.martbackend.portfolio.customer.domain.model.Customer;
import africa.springCore.martbackend.portfolio.customer.domain.repository.CustomerRepository;
import africa.springCore.martbackend.portfolio.customer.exception.CustomerUpdateFailedException;
import africa.springCore.martbackend.portfolio.product.exception.ProductCreationFailedException;
import africa.springCore.martbackend.portfolio.vendor.domain.model.Vendor;
import africa.springCore.martbackend.portfolio.vendor.domain.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static africa.springCore.martbackend.common.Message.*;
import static africa.springCore.martbackend.common.utils.AppUtils.EMAIL_VALUE;
import static africa.springCore.martbackend.common.utils.AppUtils.PHONE_NUMBER;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final MartMapper martMapper;
    private final CustomerRepository customerRepository;
    private final VendorRepository vendorRepository;
    private final BioDataRepository bioDataRepository;
    private final CloudinaryUploadService cloudinaryUploadService;

    @Override
    public CustomerResponseDto createCustomer(CustomerCreationRequest customerCreationRequest) throws MartException, CustomerCreationFailedException {

        validateCustomerCreationRequest(customerCreationRequest);
        BioData customerBioData = martMapper.readValue(customerCreationRequest, BioData.class);
        customerBioData.setRoles(List.of(Role.CUSTOMER));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(customerCreationRequest.getPassword());
        customerBioData.setPassword(encodedPassword);
        Customer customer = new Customer();
        customer.setBioData(customerBioData);
        Customer savedCustomer = customerRepository.save(customer);
        String savedCustomerAsString = martMapper.writeValueAsString(savedCustomer);
        try {
            return martMapper.readValue(savedCustomerAsString, CustomerResponseDto.class);
        } catch (Exception ex) {
            throw new MartException(ex.getMessage());
        }
    }

    private void validateCustomerCreationRequest(CustomerCreationRequest customerCreationRequest) throws CustomerCreationFailedException {
        validateEmailDuplicity(customerCreationRequest.getEmailAddress());
        if (customerCreationRequest.getPhoneNumber() != null && !StringUtils.isEmpty(customerCreationRequest.getPhoneNumber())) {
            validatePhoneNumberDuplicity(customerCreationRequest.getPhoneNumber());
        }
    }

    private void validateEmailDuplicity(String emailAddress) throws CustomerCreationFailedException {
        if (emailAddress == null || StringUtils.isEmpty(emailAddress)) {
            throw new CustomerCreationFailedException("Validation failed, emailAddress cannot be null");
        }
        Optional<Customer> foundCustomerByEmail = customerRepository.findByBioData_EmailAddress(emailAddress);
        Optional<Vendor> foundVendorByEmail = vendorRepository.findByBioData_EmailAddress(emailAddress);
        Optional<BioData> foundUserByEmail = bioDataRepository.findByEmailAddress(emailAddress);
        if (foundCustomerByEmail.isPresent()) {
            throw new CustomerCreationFailedException(
                    String.format(CUSTOMER_WITH_EMAIL_ALREADY_EXISTS, emailAddress)
            );
        }
        if (foundVendorByEmail.isPresent() || foundUserByEmail.isPresent()) {
            throw new CustomerCreationFailedException(
                    String.format(USER_WITH_EMAIL_ALREADY_EXISTS, emailAddress)
            );
        }
    }

    private void validatePhoneNumberDuplicity(String phoneNumber) throws CustomerCreationFailedException {
        if (phoneNumber == null || StringUtils.isEmpty(phoneNumber)) {
            throw new CustomerCreationFailedException("Validation failed, phone number cannot be null");
        }
        Optional<Customer> foundCustomerByNumber = customerRepository.findByBioData_PhoneNumber(phoneNumber);
        Optional<Vendor> foundVendorByNumber = vendorRepository.findByBioData_PhoneNumber(phoneNumber);
        if (foundCustomerByNumber.isPresent()) {
            throw new CustomerCreationFailedException(
                    String.format(CUSTOMER_WITH_PHONE_NUMBER_ALREADY_EXISTS, phoneNumber)
            );
        }
        if (foundVendorByNumber.isPresent()) {
            throw new CustomerCreationFailedException(
                    String.format(USER_WITH_PHONE_NUMBER_ALREADY_EXISTS, phoneNumber)
            );
        }
    }

    @Override
    public CustomerResponseDto findByEmail(String email) throws MapperException, UserNotFoundException {
        Customer foundCustomer = customerRepository.findByBioData_EmailAddress(email).orElseThrow(
                () -> new UserNotFoundException(String.format(USER_WITH_EMAIL_NOT_FOUND, email))
        );
        return getCustomerResponseDto(foundCustomer);
    }

    private CustomerResponseDto getCustomerResponseDto(Customer foundCustomer) throws MapperException {
        BioDataResponseDto bioDataResponse = martMapper.readValue(foundCustomer.getBioData(), BioDataResponseDto.class);
        CustomerResponseDto customerResponseDto = martMapper.readValue(foundCustomer, CustomerResponseDto.class);
        customerResponseDto.setBioData(bioDataResponse);
        return customerResponseDto;
    }

    @Override
    public CustomerResponseDto findById(Long id) throws MapperException, UserNotFoundException {
        Customer foundCustomer = customerRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException(String.format(USER_WITH_ID_NOT_FOUND, id))
        );
        return getCustomerResponseDto(foundCustomer);
    }

    @Override
    public CustomerListingDto retrieveAll(Pageable pageable) {
        Page<Customer> pagedCustomers = customerRepository.findAll(pageable);
        return getCustomerListingDto(pagedCustomers);
    }

    @Override
    public CustomerListingDto searchBy(String searchParam, String value, Pageable pageable) {
        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Customer criteria = new Customer();
        BioData bioData = new BioData();
        if (searchParam.equals(EMAIL_VALUE)) bioData.setEmailAddress(value);
        else if (searchParam.equals(PHONE_NUMBER)) bioData.setPhoneNumber(value);
        criteria.setBioData(bioData);
        Example<Customer> example = Example.of(criteria, matcher);
        Page<Customer> pagedCustomers = customerRepository.findAll(example, pageable);
        return getCustomerListingDto(pagedCustomers);
    }

    @Override
    public CustomerResponseDto updateCustomer(Long id, CustomerUpdateRequest customerUpdateRequest, MultipartFile file) throws CustomerCreationFailedException, UserNotFoundException, MapperException, CustomerUpdateFailedException, UserUpdateFailedException {
        boolean allFieldsAreEmpty = true;
        findById(id);
        Customer existingCustomer = customerRepository.findById(id).get();
        BioData existingCustomerBioData = customerRepository.findById(id).get().getBioData();
        if (customerUpdateRequest.getEmailAddress() != null && !StringUtils.isEmpty(customerUpdateRequest.getEmailAddress())) {
            allFieldsAreEmpty = false;
            validateEmailDuplicity(customerUpdateRequest.getEmailAddress());
            existingCustomerBioData.setEmailAddress(customerUpdateRequest.getEmailAddress());
        }
        if (customerUpdateRequest.getPhoneNumber() != null && !StringUtils.isEmpty(customerUpdateRequest.getPhoneNumber())) {
            allFieldsAreEmpty = false;
            validatePhoneNumberDuplicity(customerUpdateRequest.getPhoneNumber());
            existingCustomerBioData.setPhoneNumber(customerUpdateRequest.getPhoneNumber());
        }
        if (customerUpdateRequest.getFirstName() != null && !StringUtils.isEmpty(customerUpdateRequest.getFirstName())) {
            allFieldsAreEmpty = false;
            existingCustomerBioData.setFirstName(customerUpdateRequest.getFirstName());
        }
        if (customerUpdateRequest.getLastName() != null && !StringUtils.isEmpty(customerUpdateRequest.getLastName())) {
            allFieldsAreEmpty = false;
            existingCustomerBioData.setLastName(customerUpdateRequest.getLastName());
        }
        if (!file.isEmpty()){
            allFieldsAreEmpty = true;
            CompletableFuture.supplyAsync(() -> {
                try {
                    return existingCustomer.uploadAndAddMedia(file, cloudinaryUploadService, "profilePicture", MediaType.PICTURE);
                } catch (UserUpdateFailedException e) {
                    throw new RuntimeException(e);
                }
            }).thenApplyAsync(customerRepository::save);
        }

        if (allFieldsAreEmpty) throw new CustomerUpdateFailedException("No field specified for update");
        else {
            existingCustomer.setBioData(existingCustomerBioData);
            return getCustomerResponseDto(customerRepository.save(existingCustomer));
        }
    }

    private CustomerListingDto getCustomerListingDto(Page<Customer> pagedCustomers) {
        Page<CustomerResponseDto> customerResponseDtoPage = pagedCustomers.map((customer) -> {
            try {
                return martMapper.readValue(customer, CustomerResponseDto.class);
            } catch (MapperException e) {
                e.printStackTrace();
            }
            return null;
        });
        CustomerListingDto customerListingDto = new CustomerListingDto();
        customerListingDto.setCustomers(customerResponseDtoPage.getContent());
        customerListingDto.setPageNumber(customerResponseDtoPage.getNumber());
        customerListingDto.setPageSize(customerResponseDtoPage.getSize());
        return customerListingDto;
    }
}
