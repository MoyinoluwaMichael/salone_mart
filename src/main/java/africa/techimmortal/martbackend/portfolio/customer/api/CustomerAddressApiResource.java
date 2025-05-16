package africa.techimmortal.martbackend.portfolio.customer.api;

import africa.techimmortal.martbackend.core.domain.dtos.request.AddressCreationRequest;
import africa.techimmortal.martbackend.core.domain.dtos.response.AddressListingDto;
import africa.techimmortal.martbackend.core.domain.dtos.response.AddressResponseDto;
import africa.techimmortal.martbackend.core.service.AddressService;
import africa.techimmortal.martbackend.portfolio.customer.exception.CustomerCreationFailedException;
import africa.techimmortal.martbackend.infrastructure.exception.MartException;
import africa.techimmortal.martbackend.portfolio.customer.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static africa.techimmortal.martbackend.core.domain.enums.Role.CUSTOMER;

@RequestMapping("api/v1/customers/{customerId}/address")
@RestController
@RequiredArgsConstructor
@Validated
public class CustomerAddressApiResource {

    private final AddressService addressService;
    private final CustomerService customerService;

    @Operation(summary = "Create a new Address for a customer")
    @PostMapping("")
    public ResponseEntity<AddressResponseDto> createCustomerAddress(
            @PathVariable(name = "customerId") String customerId,
            @RequestBody AddressCreationRequest addressCreationRequest) throws MartException, CustomerCreationFailedException {
        customerService.findById(Long.valueOf(customerId));
        AddressResponseDto postAddressesResponse =
                addressService.createUserAddress(addressCreationRequest, CUSTOMER, Long.valueOf(customerId));

        return ResponseEntity.ok(postAddressesResponse);
    }

    @Operation(summary = "Find by address ID")
    @GetMapping("/{id}")
    public ResponseEntity<AddressResponseDto> findById(
            @PathVariable(name = "customerId") Long customerId,
            @PathVariable(name = "id") Long id) throws MartException {

        customerService.findById(customerId);
        AddressResponseDto postAddressesResponse =
                addressService.findById(id, CUSTOMER);

        return ResponseEntity.ok(postAddressesResponse);
    }

    @Operation(summary = "Find Customer id by ID")
    @GetMapping("")
    public ResponseEntity<AddressListingDto> findByCustomerId(
            @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @PathVariable(name = "customerId") Long customerId) throws MartException {

        customerService.findById(customerId);
        AddressListingDto postAddressesResponse =
                addressService.findByUserId(customerId, CUSTOMER, pageable);

        return ResponseEntity.ok(postAddressesResponse);
    }

    @Operation(summary = "Update customer address")
    @PatchMapping("/{addressId}")
    public ResponseEntity<AddressResponseDto> updateCustomer(
            @PathVariable(name = "customerId") Long customerId,
            @PathVariable(name = "addressId") Long addressId,
            @Valid @RequestBody AddressCreationRequest addressCreationRequest
    ) throws MartException {

        customerService.findById(customerId);
        AddressResponseDto postAddressesResponse =
                addressService.updateUserAddress(addressCreationRequest, customerId, CUSTOMER, addressId);

        return ResponseEntity.ok(postAddressesResponse);
    }
}
