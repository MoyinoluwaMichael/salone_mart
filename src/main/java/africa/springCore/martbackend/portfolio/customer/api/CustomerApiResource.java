package africa.springCore.martbackend.portfolio.customer.api;


import africa.springCore.martbackend.core.portfolio.customer.domain.dtos.requests.CustomerCreationRequest;
import africa.springCore.martbackend.core.portfolio.customer.domain.dtos.requests.CustomerUpdateRequest;
import africa.springCore.martbackend.core.portfolio.customer.exception.CustomerCreationFailedException;
import africa.springCore.martbackend.infrastructure.exception.MartException;
import africa.springCore.martbackend.infrastructure.exception.MapperException;
import africa.springCore.martbackend.infrastructure.exception.UserNotFoundException;
import africa.springCore.martbackend.portfolio.customer.domain.dtos.responses.CustomerListingDto;
import africa.springCore.martbackend.portfolio.customer.domain.dtos.responses.CustomerResponseDto;
import africa.springCore.martbackend.portfolio.customer.exception.CustomerUpdateFailedException;
import africa.springCore.martbackend.portfolio.customer.service.CustomerService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("api/v1/customers")
@RestController
@RequiredArgsConstructor
@Validated
public class CustomerApiResource {

    private final CustomerService customerService;

    @Operation(summary = "Create a New Customer")
    @PostMapping("")
    public ResponseEntity<CustomerResponseDto> createCustomer(
            @Valid @RequestBody CustomerCreationRequest customerCreationRequest
    ) throws MartException, CustomerCreationFailedException {

        CustomerResponseDto postClientsResponse =
                customerService.createCustomer(customerCreationRequest);

        return ResponseEntity.ok(postClientsResponse);
    }

    @Operation(summary = "Find by ID")
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> findById(
            @PathVariable(name = "id") Long id
    ) throws MapperException, UserNotFoundException {

        CustomerResponseDto customer =
                customerService.findById(id);

        return ResponseEntity.ok(customer);
    }


    @Operation(summary = "Find by Email")
    @GetMapping("/search")
    public ResponseEntity<CustomerListingDto> findByEmail(
            @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(name = "searchParam", defaultValue = "email") String searchParam,
            @RequestParam(name = "value") String value
    ) throws MapperException {

        CustomerListingDto customers =
                customerService.searchBy(searchParam, value, pageable);

        return ResponseEntity.ok(customers);
    }

    @Operation(summary = "Retrieve all customers")
    @GetMapping("")
    public ResponseEntity<CustomerListingDto> retrieveAll(
            @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
    ) throws MapperException {

        CustomerListingDto customers =
                customerService.retrieveAll(pageable);

        return ResponseEntity.ok(customers);
    }

    @Operation(summary = "Update a customer")
    @PatchMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> updateCustomer(
            @Valid @PathVariable(name = "id") Long id,
            @Valid @RequestBody CustomerUpdateRequest customerUpdateRequest
    ) throws MartException, CustomerCreationFailedException, CustomerUpdateFailedException {
        {
            CustomerResponseDto customer =
                    customerService.updateCustomer(id, customerUpdateRequest);

            return ResponseEntity.ok(customer);
        }
    }
}
