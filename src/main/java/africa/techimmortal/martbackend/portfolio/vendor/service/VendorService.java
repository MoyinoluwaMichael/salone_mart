package africa.techimmortal.martbackend.portfolio.vendor.service;

import africa.techimmortal.martbackend.core.domain.dtos.response.BasePageableResponse;
import africa.techimmortal.martbackend.portfolio.vendor.exception.VendorApprovalFailedException;
import africa.techimmortal.martbackend.portfolio.vendor.exception.VendorCreationException;
import africa.techimmortal.martbackend.portfolio.vendor.exception.VendorUpdateException;
import africa.techimmortal.martbackend.infrastructure.exception.MartException;
import africa.techimmortal.martbackend.infrastructure.exception.MapperException;
import africa.techimmortal.martbackend.infrastructure.exception.UserNotFoundException;
import africa.techimmortal.martbackend.portfolio.vendor.domain.dtos.requests.VendorCreationRequest;
import africa.techimmortal.martbackend.portfolio.vendor.domain.dtos.requests.VendorUpdateRequest;
import africa.techimmortal.martbackend.portfolio.vendor.domain.dtos.responses.VendorResponseDto;
import org.springframework.data.domain.Pageable;

public interface VendorService {
    VendorResponseDto findByEmail(String emailAddress) throws MapperException, UserNotFoundException;
    VendorResponseDto createVendor(VendorCreationRequest VendorCreationRequest) throws MartException, VendorCreationException;

    VendorResponseDto findById(Long id) throws UserNotFoundException, MapperException;

    BasePageableResponse<VendorResponseDto> retrieveAll(Pageable pageable) throws MapperException;

    BasePageableResponse<VendorResponseDto> searchBy(String searchParam, String value, Pageable pageable);

    VendorResponseDto updateVendor(Long id, VendorUpdateRequest VendorUpdateRequest) throws VendorCreationException, UserNotFoundException, MapperException, VendorUpdateException;

    VendorResponseDto approveVendor(Long id, String actionName) throws UserNotFoundException, MapperException, VendorApprovalFailedException;

    Long retrieveTotalVendors();
}
