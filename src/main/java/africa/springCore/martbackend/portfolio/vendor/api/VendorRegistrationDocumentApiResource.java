package africa.springCore.martbackend.core.portfolio.vendor.api;

import africa.springCore.martbackend.core.portfolio.vendor.domain.dtos.requests.VendorRegistrationDocumentCreationRequest;
import africa.springCore.martbackend.core.portfolio.vendor.domain.dtos.responses.VendorRegistrationDocumentListingDto;
import africa.springCore.martbackend.core.portfolio.vendor.domain.model.VendorRegistrationDocument;
import africa.springCore.martbackend.core.portfolio.vendor.service.VendorRegistrationDocumentService;
import africa.springCore.martbackend.infrastructure.exception.MapperException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/v1/vendors/{vendorId}/documents")
@RestController
@RequiredArgsConstructor
@Validated
public class VendorRegistrationDocumentApiResource {

    private final VendorRegistrationDocumentService documentService;

    @PostMapping("")
    public ResponseEntity<VendorRegistrationDocument> postVendorRegistrationDocument(
            @PathVariable(name = "vendorId") Long vendorId,
            @RequestBody VendorRegistrationDocumentCreationRequest documentCreationRequest
            ) throws MapperException {

        VendorRegistrationDocument postDocumentResponse =
                documentService.postVendorRegistrationDocument(vendorId, documentCreationRequest);

        return ResponseEntity.ok(postDocumentResponse);
    }

    @GetMapping("")
    public ResponseEntity<VendorRegistrationDocumentListingDto> getVendorRegistrationDocuments(
            @PathVariable(name = "vendorId") Long vendorId,
            @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
            ) throws MapperException {

        VendorRegistrationDocumentListingDto documentListingDto =
                documentService.getVendorRegistrationDocuments(vendorId, pageable);

        return ResponseEntity.ok(documentListingDto);
    }
}
