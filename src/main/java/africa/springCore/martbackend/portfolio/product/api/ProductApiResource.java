package africa.springCore.martbackend.portfolio.product.api;

import africa.springCore.martbackend.core.domain.dtos.response.BasePageableResponse;
import africa.springCore.martbackend.core.portfolio.product.exception.ProductNotFoundException;
import africa.springCore.martbackend.infrastructure.exception.MapperException;
import africa.springCore.martbackend.infrastructure.exception.UserNotFoundException;
import africa.springCore.martbackend.portfolio.product.domain.dtos.request.ProductCreationRequest;
import africa.springCore.martbackend.portfolio.product.domain.model.Product;
import africa.springCore.martbackend.portfolio.product.exception.ProductClassificationNotFoundException;
import africa.springCore.martbackend.portfolio.product.exception.ProductCreationFailedException;
import africa.springCore.martbackend.portfolio.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/v1/")
@RestController
@RequiredArgsConstructor
@Validated
public class ProductApiResource {

    private final ProductService productService;

    @PostMapping("vendors/{vendorId}/products")
    @Operation(summary = "Create a New Product")
    public ResponseEntity<Product> postAProduct(
            @PathVariable(name = "vendorId") Long vendorId,
            @Valid @RequestBody ProductCreationRequest productCreationRequest
    ) throws UserNotFoundException, ProductCreationFailedException, MapperException, ProductClassificationNotFoundException {
        Product postProductResponse = productService.postAProduct(vendorId, productCreationRequest);
        return ResponseEntity.ok(postProductResponse);
    }

    @GetMapping("products/{id}")
    @Operation(summary = "Get product by ID")
    public ResponseEntity<Product> getProductById(
            @PathVariable(name = "id") Long id
    ) throws MapperException, ProductNotFoundException {
        Product postProductResponse = productService.getProductById(id);
        return ResponseEntity.ok(postProductResponse);
    }

    @GetMapping("products")
    @Operation(summary = "Get all products")
    public ResponseEntity<BasePageableResponse<Product>> getAllProducts(
            @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        BasePageableResponse<Product> postProductResponse = productService.getAllProducts(pageable);
        return ResponseEntity.ok(postProductResponse);
    }

    @GetMapping("products/search")
    @Operation(summary = "Search products by name or category name")
    public ResponseEntity<BasePageableResponse<Product>> searchProducts(
            @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(name = "searchParam", defaultValue = "productName") String searchParam,
            @RequestParam(name = "value") String value
    ) throws MapperException {
        BasePageableResponse<Product> postProductResponse = productService.searchProducts(searchParam, value, pageable);
        return ResponseEntity.ok(postProductResponse);
    }
}
