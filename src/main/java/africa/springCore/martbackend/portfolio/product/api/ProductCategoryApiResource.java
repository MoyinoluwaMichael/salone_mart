package africa.springCore.martbackend.portfolio.product.api;

import africa.springCore.martbackend.core.domain.dtos.response.BasePageableResponse;
import africa.springCore.martbackend.infrastructure.exception.MapperException;
import africa.springCore.martbackend.portfolio.product.domain.dtos.request.ProductClassificationCreationRequest;
import africa.springCore.martbackend.portfolio.product.domain.model.ProductCategory;
import africa.springCore.martbackend.portfolio.product.exception.ProductClassificationNotFoundException;
import africa.springCore.martbackend.portfolio.product.service.ProductCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/v1/products/categories")
@RestController
@RequiredArgsConstructor
@Validated
public class ProductCategoryApiResource {


    private final ProductCategoryService productCategoryService;

    @PostMapping("")
    @Operation(summary = "Create a New Product Category")
    public ResponseEntity<ProductCategory> postAProductCategory(
            @Valid @RequestBody ProductClassificationCreationRequest productClassificationCreationRequest
    ) throws MapperException {
        ProductCategory postProductCategoryResponse = productCategoryService.postAProductCategory(productClassificationCreationRequest);
        return ResponseEntity.ok(postProductCategoryResponse);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find a Product Category by ID")
    public ResponseEntity<ProductCategory> getAProductCategoryId(
            @PathVariable(name = "id") Long id
    ) throws ProductClassificationNotFoundException, MapperException {
        ProductCategory postProductCategoryResponse = productCategoryService.findById(id);
        return ResponseEntity.ok(postProductCategoryResponse);
    }

    @GetMapping("")
    @Operation(summary = "Find all Product Categories")
    public ResponseEntity<BasePageableResponse<ProductCategory>> getAllProductCategories(
            @PageableDefault(size = 20, page = 0, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        BasePageableResponse<ProductCategory> postProductCategoryResponse = productCategoryService.getAllProductCategories(pageable);
        return ResponseEntity.ok(postProductCategoryResponse);
    }
}
