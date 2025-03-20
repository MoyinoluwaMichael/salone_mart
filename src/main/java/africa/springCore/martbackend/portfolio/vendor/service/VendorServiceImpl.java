package africa.springCore.martbackend.portfolio.vendor.service;

import africa.springCore.martbackend.common.enums.ApprovalStatus;
import africa.springCore.martbackend.core.base.domain.dtos.response.BioDataResponseDto;
import africa.springCore.martbackend.core.base.domain.model.BioData;
import africa.springCore.martbackend.core.base.domain.model.Media;
import africa.springCore.martbackend.core.base.domain.model.MediaType;
import africa.springCore.martbackend.core.base.domain.repository.BioDataRepository;
import africa.springCore.martbackend.common.enums.Role;
import africa.springCore.martbackend.core.portfolio.vendor.exception.VendorApprovalFailedException;
import africa.springCore.martbackend.core.portfolio.vendor.exception.VendorCreationException;
import africa.springCore.martbackend.core.portfolio.vendor.exception.VendorUpdateException;
import africa.springCore.martbackend.common.utils.MartMapper;
import africa.springCore.martbackend.infrastructure.cloudservice.storageservice.service.CloudinaryUploadService;
import africa.springCore.martbackend.infrastructure.exception.MartException;
import africa.springCore.martbackend.infrastructure.exception.MapperException;
import africa.springCore.martbackend.infrastructure.exception.UserNotFoundException;
import africa.springCore.martbackend.infrastructure.exception.UserUpdateFailedException;
import africa.springCore.martbackend.portfolio.vendor.domain.dtos.requests.FileMetaData;
import africa.springCore.martbackend.portfolio.vendor.domain.dtos.requests.VendorCreationRequest;
import africa.springCore.martbackend.portfolio.vendor.domain.dtos.requests.VendorUpdateRequest;
import africa.springCore.martbackend.portfolio.vendor.domain.dtos.responses.VendorListingDto;
import africa.springCore.martbackend.portfolio.vendor.domain.dtos.responses.VendorResponseDto;
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
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static africa.springCore.martbackend.common.Message.USER_WITH_EMAIL_ALREADY_EXISTS;
import static africa.springCore.martbackend.common.Message.USER_WITH_EMAIL_NOT_FOUND;
import static africa.springCore.martbackend.common.Message.USER_WITH_ID_NOT_FOUND;
import static africa.springCore.martbackend.common.Message.USER_WITH_PHONE_NUMBER_ALREADY_EXISTS;
import static africa.springCore.martbackend.common.Message.VENDOR_WITH_EMAIL_ALREADY_EXISTS;
import static africa.springCore.martbackend.common.Message.VENDOR_WITH_PHONE_NUMBER_ALREADY_EXISTS;
import static africa.springCore.martbackend.common.utils.AppUtils.APPROVE;
import static africa.springCore.martbackend.common.utils.AppUtils.EMAIL_VALUE;
import static africa.springCore.martbackend.common.utils.AppUtils.FIRST_NAME;
import static africa.springCore.martbackend.common.utils.AppUtils.LAST_NAME;
import static africa.springCore.martbackend.common.utils.AppUtils.PHONE_NUMBER;
import static africa.springCore.martbackend.common.utils.AppUtils.REJECT;

@Service
@RequiredArgsConstructor
public class VendorServiceImpl implements VendorService {

    private final MartMapper martMapper;
    private final VendorRepository vendorRepository;
    private final BioDataRepository bioDataRepository;
    private final CloudinaryUploadService cloudinaryUploadService;

    @Override
    public VendorResponseDto findByEmail(String emailAddress) throws MapperException, UserNotFoundException {
        Vendor foundVendor = vendorRepository.findByBioData_EmailAddress(emailAddress).orElseThrow(
                ()-> new UserNotFoundException(String.format(USER_WITH_EMAIL_NOT_FOUND, emailAddress))
        );
        BioDataResponseDto bioDataResponse = martMapper.readValue(foundVendor.getBioData(), BioDataResponseDto.class);
        VendorResponseDto vendorResponseDto = martMapper.readValue(foundVendor, VendorResponseDto.class);
        vendorResponseDto.setBioData(bioDataResponse);
        return vendorResponseDto;
    }

    @Override
    public VendorResponseDto createVendor(VendorCreationRequest vendorCreationRequest, List<MultipartFile> files) throws MartException, VendorCreationException {
        validateVendorCreationRequest(vendorCreationRequest);
        BioData vendorBioData = martMapper.readValue(vendorCreationRequest, BioData.class);
        vendorBioData.setRoles(List.of(Role.VENDOR));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(vendorCreationRequest.getPassword());
        vendorBioData.setPassword(encodedPassword);
        Vendor vendor = new Vendor();
        vendor.setBusinessName(vendorCreationRequest.getBusinessName());
        vendor.setBioData(vendorBioData);
        vendor.setApprovalStatus(ApprovalStatus.PENDING_REVIEW);
        Vendor savedVendor = vendorRepository.save(vendor);

        Map<String, FileMetaData> metadataMap = vendorCreationRequest.getFiles().stream()
                .collect(Collectors.toMap(FileMetaData::getId, metadata -> metadata));

        List<CompletableFuture<Void>> futures = files.stream()
                .map(file -> CompletableFuture.runAsync(() -> {
                    String fileId = file.getOriginalFilename();
                    FileMetaData metadata = metadataMap.get(fileId);
                    if (metadata != null) {
                        uploadAndAddMedia(savedVendor, file, metadata);
                    }
                }))
                .toList();
        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allOf.thenRunAsync(() -> vendorRepository.save(savedVendor)).join();


        String savedVendorAsString = martMapper.writeValueAsString(savedVendor);
        try {
            return martMapper.readValue(savedVendorAsString, VendorResponseDto.class);
        } catch (Exception ex) {
            throw new MartException(ex.getMessage());
        }
    }

    private void validateVendorCreationRequest(VendorCreationRequest VendorCreationRequest) throws VendorCreationException {
        validateEmailDuplicity(VendorCreationRequest.getEmailAddress());
        if (VendorCreationRequest.getPhoneNumber() != null && !StringUtils.isEmpty(VendorCreationRequest.getPhoneNumber())) {
            validatePhoneNumberDuplicity(VendorCreationRequest.getPhoneNumber());
        }
    }

    private void validateEmailDuplicity(String emailAddress) throws VendorCreationException {
        if (emailAddress == null || StringUtils.isEmpty(emailAddress)) {
            throw new VendorCreationException("Validation failed, emailAddress cannot be null");
        }
        Optional<Vendor> foundVendorByEmail = vendorRepository.findByBioData_EmailAddress(emailAddress);
        Optional<BioData> foundCustomerByEmail = bioDataRepository.findByEmailAddress(emailAddress);
        if (foundVendorByEmail.isPresent()) {
            throw new VendorCreationException(
                    String.format(VENDOR_WITH_EMAIL_ALREADY_EXISTS, emailAddress)
            );
        }
        if (foundCustomerByEmail.isPresent()) {
            throw new VendorCreationException(
                    String.format(USER_WITH_EMAIL_ALREADY_EXISTS, emailAddress)
            );
        }
    }

    private void validatePhoneNumberDuplicity(String phoneNumber) throws VendorCreationException {
        if (phoneNumber == null || StringUtils.isEmpty(phoneNumber)) {
            throw new VendorCreationException("Validation failed, phone number cannot be null");
        }
        Optional<Vendor> foundVendorByNumber = vendorRepository.findByBioData_PhoneNumber(phoneNumber);
        Optional<BioData> foundCustomerByNumber = bioDataRepository.findByEmailAddress(phoneNumber);
        if (foundVendorByNumber.isPresent()) {
            throw new VendorCreationException(
                    String.format(VENDOR_WITH_PHONE_NUMBER_ALREADY_EXISTS, phoneNumber)
            );
        }
        if (foundCustomerByNumber.isPresent()) {
            throw new VendorCreationException(
                    String.format(USER_WITH_PHONE_NUMBER_ALREADY_EXISTS, phoneNumber)
            );
        }
    }

    private VendorResponseDto getVendorResponseDto(Vendor foundVendor) throws MapperException {
        BioDataResponseDto bioDataResponse = martMapper.readValue(foundVendor.getBioData(), BioDataResponseDto.class);
        VendorResponseDto VendorResponseDto = martMapper.readValue(foundVendor, VendorResponseDto.class);
        VendorResponseDto.setBioData(bioDataResponse);
        return VendorResponseDto;
    }

    @Override
    public VendorResponseDto findById(Long id) throws MapperException, UserNotFoundException {
        Vendor foundVendor = vendorRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException(String.format(USER_WITH_ID_NOT_FOUND, id))
        );
        return getVendorResponseDto(foundVendor);
    }

    @Override
    public VendorListingDto retrieveAll(Pageable pageable) {
        Page<VendorResponseDto> pagedVendors = vendorRepository.findAll(pageable).map((vendor) -> {
            try {
                return martMapper.readValue(vendor, VendorResponseDto.class);
            } catch (MapperException e) {
                e.printStackTrace();
            }
            return null;
        });
        return getVendorListingDto(pagedVendors, pageable);
    }

    @Override
    public VendorListingDto searchBy(String searchParam, String value, Pageable pageable) {
        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Vendor criteria = new Vendor();
        BioData bioData = new BioData();
        if (searchParam.equals(EMAIL_VALUE)) bioData.setEmailAddress(value);
        else if (searchParam.equals(PHONE_NUMBER)) bioData.setPhoneNumber(value);
        else if (searchParam.equals(FIRST_NAME)) bioData.setFirstName(value);
        else if (searchParam.equals(LAST_NAME)) bioData.setLastName(value);
        criteria.setBioData(bioData);
        Example<Vendor> example = Example.of(criteria, matcher);
        Page<VendorResponseDto> pagedVendors = vendorRepository.findAll(example, pageable).map((vendor) -> {
            try {
                return martMapper.readValue(vendor, VendorResponseDto.class);
            } catch (MapperException e) {
                e.printStackTrace();
            }
            return null;
        });
        return getVendorListingDto(pagedVendors, pageable);
    }

    @Override
    public VendorResponseDto updateVendor(Long id, VendorUpdateRequest vendorUpdateRequest, List<MultipartFile> files) throws VendorCreationException, UserNotFoundException, MapperException, VendorUpdateException {
        boolean allFieldsAreEmpty = true;
        findById(id);
        Vendor existingVendor = vendorRepository.findById(id).orElseThrow(() -> new UserNotFoundException(String.format(USER_WITH_ID_NOT_FOUND, id)));
        BioData existingVendorBioData = existingVendor.getBioData();

        if (vendorUpdateRequest.getEmailAddress() != null && !StringUtils.isEmpty(vendorUpdateRequest.getEmailAddress())) {
            allFieldsAreEmpty = false;
            validateEmailDuplicity(vendorUpdateRequest.getEmailAddress());
            existingVendorBioData.setEmailAddress(vendorUpdateRequest.getEmailAddress());
        }
        if (vendorUpdateRequest.getPhoneNumber() != null && !StringUtils.isEmpty(vendorUpdateRequest.getPhoneNumber())) {
            allFieldsAreEmpty = false;
            validatePhoneNumberDuplicity(vendorUpdateRequest.getPhoneNumber());
            existingVendorBioData.setPhoneNumber(vendorUpdateRequest.getPhoneNumber());
        }
        if (vendorUpdateRequest.getFirstName() != null && !StringUtils.isEmpty(vendorUpdateRequest.getFirstName())) {
            allFieldsAreEmpty = false;
            existingVendorBioData.setFirstName(vendorUpdateRequest.getFirstName());
        }
        if (vendorUpdateRequest.getLastName() != null && !StringUtils.isEmpty(vendorUpdateRequest.getLastName())) {
            allFieldsAreEmpty = false;
            existingVendorBioData.setLastName(vendorUpdateRequest.getLastName());
        }

        if (!files.isEmpty()) {
            allFieldsAreEmpty = false;
            List<CompletableFuture<Void>> futures = files.stream()
                    .map(file -> CompletableFuture.runAsync(() -> {
                        String fileId = file.getOriginalFilename();
                        FileMetaData metadata = vendorUpdateRequest.getFiles().stream()
                                .filter(meta -> meta.getId().equals(fileId))
                                .findFirst()
                                .orElse(null);
                        if (metadata != null) {
                            Media media = existingVendor.getBioData().getMediaByPurpose(metadata.getKey());
                            if (media != null) {
                                try {
                                    existingVendor.getBioData().removeMedia(media, cloudinaryUploadService);
                                } catch (UserUpdateFailedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            uploadAndAddMedia(existingVendor, file, metadata);
                        }
                    }))
                    .toList();
            CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
            allOf.thenRunAsync(() -> vendorRepository.save(existingVendor)).join();
        }

        if (allFieldsAreEmpty) throw new VendorUpdateException("No field specified for update");
        else {
            existingVendor.setBioData(existingVendorBioData);
            return getVendorResponseDto(vendorRepository.save(existingVendor));
        }
    }

    private void uploadAndAddMedia(Vendor existingVendor, MultipartFile file, FileMetaData metadata) {
        String contentType = file.getContentType();
        try {
            if (contentType != null && contentType.startsWith("image/")) {
                existingVendor.uploadAndAddMedia(file, cloudinaryUploadService, metadata.getKey(), MediaType.PICTURE);
            } else {
                existingVendor.uploadAndAddMedia(file, cloudinaryUploadService, metadata.getKey(), MediaType.DOCUMENT);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while uploading file");
        }
    }

    @Override
    public VendorResponseDto approveVendor(Long id, String actionName) throws UserNotFoundException, MapperException, VendorApprovalFailedException {
        VendorResponseDto vendorResponseDto = findById(id);
        if (vendorResponseDto.getApprovalStatus() == ApprovalStatus.APPROVED){
            throw new VendorApprovalFailedException("Vendor is already in approved state.");
        }
        Vendor existingVendor = vendorRepository.findById(id).get();
        if (actionName.equalsIgnoreCase(APPROVE)) existingVendor.setApprovalStatus(ApprovalStatus.APPROVED);
        if (actionName.equalsIgnoreCase(REJECT)) existingVendor.setApprovalStatus(ApprovalStatus.REJECTED);
        return getVendorResponseDto(vendorRepository.save(existingVendor));
    }

    private VendorListingDto getVendorListingDto(Page<VendorResponseDto> pagedVendors, Pageable pageable) {
        VendorListingDto vendorListingDto = new VendorListingDto();
        vendorListingDto.setVendors(pagedVendors.getContent());
        vendorListingDto.setPageNumber(pageable.getPageNumber());
        vendorListingDto.setPageSize(pageable.getPageSize());
        return vendorListingDto;
    }
}
