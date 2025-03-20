package africa.springCore.martbackend.portfolio.product.service;

import africa.springCore.martbackend.core.portfolio.product.exception.ProductCategoryNotFoundException;
import africa.springCore.martbackend.core.portfolio.product.exception.ProductNotFoundException;
import africa.springCore.martbackend.infrastructure.exception.MapperException;
import africa.springCore.martbackend.infrastructure.exception.UserNotFoundException;
import africa.springCore.martbackend.portfolio.product.domain.dtos.request.ProductCreationRequest;
import africa.springCore.martbackend.portfolio.product.domain.dtos.response.ProductListingDto;
import africa.springCore.martbackend.portfolio.product.domain.dtos.response.ProductResponseDto;
import africa.springCore.martbackend.portfolio.product.exception.ProductCreationFailedException;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {
    ProductResponseDto postAProduct(Long vendorId, ProductCreationRequest productCreationRequest, MultipartFile file) throws UserNotFoundException, MapperException, ProductCategoryNotFoundException, ProductCreationFailedException;

    ProductListingDto getAllProducts(Pageable pageable);

    ProductResponseDto getProductById(Long id) throws ProductNotFoundException, MapperException, ProductCategoryNotFoundException;

    ProductListingDto getVendorProducts(Long vendorId, Pageable pageable);

    ProductListingDto searchProducts(String searchParam, String value, Pageable pageable) throws ProductCategoryNotFoundException, MapperException;
}
