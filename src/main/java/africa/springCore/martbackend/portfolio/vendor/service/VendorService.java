package africa.springCore.martbackend.portfolio.vendor.service;

import africa.springCore.martbackend.core.portfolio.vendor.exception.VendorApprovalFailedException;
import africa.springCore.martbackend.core.portfolio.vendor.exception.VendorCreationException;
import africa.springCore.martbackend.core.portfolio.vendor.exception.VendorUpdateException;
import africa.springCore.martbackend.infrastructure.exception.MartException;
import africa.springCore.martbackend.infrastructure.exception.MapperException;
import africa.springCore.martbackend.infrastructure.exception.UserNotFoundException;
import africa.springCore.martbackend.portfolio.vendor.domain.dtos.requests.VendorCreationRequest;
import africa.springCore.martbackend.portfolio.vendor.domain.dtos.requests.VendorUpdateRequest;
import africa.springCore.martbackend.portfolio.vendor.domain.dtos.responses.VendorListingDto;
import africa.springCore.martbackend.portfolio.vendor.domain.dtos.responses.VendorResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VendorService {
    VendorResponseDto findByEmail(String emailAddress) throws MapperException, UserNotFoundException;
    VendorResponseDto createVendor(VendorCreationRequest VendorCreationRequest, List<MultipartFile> files) throws MartException, VendorCreationException;

    VendorResponseDto findById(Long id) throws UserNotFoundException, MapperException;

    VendorListingDto retrieveAll(Pageable pageable) throws MapperException;

    VendorListingDto searchBy(String searchParam, String value, Pageable pageable);

    VendorResponseDto updateVendor(Long id, VendorUpdateRequest VendorUpdateRequest, List<MultipartFile> files) throws VendorCreationException, UserNotFoundException, MapperException, VendorUpdateException;

    VendorResponseDto approveVendor(Long id, String actionName) throws UserNotFoundException, MapperException, VendorApprovalFailedException;
}
