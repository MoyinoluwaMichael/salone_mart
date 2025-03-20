package africa.springCore.martbackend.portfolio.vendor.api;

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
import africa.springCore.martbackend.portfolio.vendor.service.VendorService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("api/v1/vendors")
@RestController
@RequiredArgsConstructor
@Validated
public class VendorApiResource {

    private final VendorService vendorService;

    @Operation(summary = "Create a New Vendor")
    @PostMapping("")
    public ResponseEntity<VendorResponseDto> createVendor(
            @Valid @RequestBody VendorCreationRequest vendorCreationRequest,
            @RequestPart("files") List<MultipartFile> files
    ) throws MartException, VendorCreationException {

        VendorResponseDto postClientsResponse =
                vendorService.createVendor(vendorCreationRequest, files);

        return ResponseEntity.ok(postClientsResponse);
    }

    @Operation(summary = "Find by ID")
    @GetMapping("/{id}")
    public ResponseEntity<VendorResponseDto> findById(
            @PathVariable(name = "id") Long id
    ) throws MapperException, UserNotFoundException {

        VendorResponseDto vendor =
                vendorService.findById(id);

        return ResponseEntity.ok(vendor);
    }


    @Operation(summary = "Find by Email")
    @GetMapping("/search")
    public ResponseEntity<VendorListingDto> findByEmail(
            @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(name = "searchParam", defaultValue = "email") String searchParam,
            @RequestParam(name = "value") String value
    ) throws MapperException {

        VendorListingDto vendors =
                vendorService.searchBy(searchParam, value, pageable);

        return ResponseEntity.ok(vendors);
    }

    @Operation(summary = "Retrieve all Vendors")
    @GetMapping("")
    public ResponseEntity<VendorListingDto> retrieveAll(
            @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
    ) throws MapperException {

        VendorListingDto vendors =
                vendorService.retrieveAll(pageable);

        return ResponseEntity.ok(vendors);
    }

    @Operation(summary = "Update a Vendor")
    @PatchMapping("/{id}")
    public ResponseEntity<VendorResponseDto> updateVendor(
            @Valid @PathVariable(name = "id") Long id,
            @Valid @RequestBody VendorUpdateRequest VendorUpdateRequest,
            List<MultipartFile> files
    ) throws MartException, VendorCreationException, VendorUpdateException {
        {
            VendorResponseDto vendor =
                    vendorService.updateVendor(id, VendorUpdateRequest, files);

            return ResponseEntity.ok(vendor);
        }
    }

    @Operation(summary = "Approve or reject a Vendor")
    @PostMapping("/{id}/approval")
    public ResponseEntity<VendorResponseDto> approveVendor(
            @Valid @PathVariable(name = "id") Long id,
            @RequestParam(name = "actionName", defaultValue = "reject") String actionName
    ) throws UserNotFoundException, MapperException, VendorApprovalFailedException {
        {
            VendorResponseDto vendor =
                    vendorService.approveVendor(id, actionName);

            return ResponseEntity.ok(vendor);
        }
    }
}
