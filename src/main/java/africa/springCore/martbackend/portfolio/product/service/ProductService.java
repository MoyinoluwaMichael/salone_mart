package africa.springCore.martbackend.portfolio.product.service;

import africa.springCore.martbackend.core.domain.dtos.response.BasePageableResponse;
import africa.springCore.martbackend.core.portfolio.product.exception.ProductNotFoundException;
import africa.springCore.martbackend.infrastructure.exception.EntityValidationException;
import africa.springCore.martbackend.infrastructure.exception.MapperException;
import africa.springCore.martbackend.infrastructure.exception.UserNotFoundException;
import africa.springCore.martbackend.portfolio.product.domain.dtos.request.ProductCreationRequest;
import africa.springCore.martbackend.portfolio.product.domain.dtos.request.ProductUpdateRequest;
import africa.springCore.martbackend.portfolio.product.domain.model.Product;
import africa.springCore.martbackend.portfolio.product.exception.ProductClassificationNotFoundException;
import africa.springCore.martbackend.portfolio.product.exception.ProductCreationFailedException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    Product postAProduct(ProductCreationRequest productCreationRequest) throws UserNotFoundException, MapperException, ProductCreationFailedException, ProductClassificationNotFoundException;

    BasePageableResponse<Product> getAllProducts(Pageable pageable);

    Product getProductById(Long id) throws ProductNotFoundException, MapperException;

    BasePageableResponse<Product> getVendorProducts(Long vendorId, Pageable pageable);

    BasePageableResponse<Product> searchProducts(String searchParam, String value, Pageable pageable) throws MapperException;

    Long retrieveTotalProducts();

    Product updateProductById(Long id, @Valid ProductUpdateRequest productUpdateRequest) throws ProductNotFoundException, UserNotFoundException, MapperException, EntityValidationException;
}
