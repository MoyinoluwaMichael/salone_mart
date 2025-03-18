package africa.springCore.martbackend.portfolio.vendor.service;

import africa.springCore.martbackend.core.portfolio.vendor.domain.dtos.requests.VendorRegistrationDocumentCreationRequest;
import africa.springCore.martbackend.core.portfolio.vendor.domain.dtos.responses.VendorRegistrationDocumentListingDto;
import africa.springCore.martbackend.core.portfolio.vendor.domain.model.VendorRegistrationDocument;
import africa.springCore.martbackend.infrastructure.exception.MapperException;
import org.springframework.data.domain.Pageable;

public interface VendorRegistrationDocumentService {

    VendorRegistrationDocument postVendorRegistrationDocument(Long vendorId, VendorRegistrationDocumentCreationRequest documentCreationRequest) throws MapperException;

    VendorRegistrationDocumentListingDto getVendorRegistrationDocuments(Long vendorId, Pageable pageable);
}
