package africa.springCore.martbackend.core.portfolio.vendor.service;

import africa.springCore.martbackend.common.utils.MartMapper;
import africa.springCore.martbackend.core.portfolio.vendor.domain.dtos.requests.VendorRegistrationDocumentCreationRequest;
import africa.springCore.martbackend.core.portfolio.vendor.domain.dtos.responses.VendorRegistrationDocumentListingDto;
import africa.springCore.martbackend.core.portfolio.vendor.domain.model.VendorRegistrationDocument;
import africa.springCore.martbackend.core.portfolio.vendor.domain.repository.VendorRegistrationDocumentRepository;
import africa.springCore.martbackend.infrastructure.exception.MapperException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VendorRegistrationDocumentServiceImpl implements VendorRegistrationDocumentService {

    private final MartMapper martMapper;
    private final VendorRegistrationDocumentRepository documentRepository;


    @Override
    public VendorRegistrationDocument postVendorRegistrationDocument(Long vendorId, VendorRegistrationDocumentCreationRequest documentCreationRequest) throws MapperException {
        VendorRegistrationDocument vendorRegistrationDocument = martMapper.readValue(documentCreationRequest, VendorRegistrationDocument.class);
        vendorRegistrationDocument.setVendorId(vendorId);
        return documentRepository.save(vendorRegistrationDocument);
    }

    @Override
    public VendorRegistrationDocumentListingDto getVendorRegistrationDocuments(Long vendorId, Pageable pageable) {
        Page<VendorRegistrationDocument> pagedDocuments = documentRepository.findByVendorId(vendorId, pageable);
        VendorRegistrationDocumentListingDto documentListingDto = new VendorRegistrationDocumentListingDto();
        documentListingDto.setPageNumber(pagedDocuments.getNumber());
        documentListingDto.setPageSize(pagedDocuments.getSize());
        documentListingDto.setVendorRegistrationDocuments(pagedDocuments.getContent());
        return documentListingDto;
    }
}
