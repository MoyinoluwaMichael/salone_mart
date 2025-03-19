package africa.springCore.martbackend.portfolio.product.service;

import africa.springCore.martbackend.core.portfolio.product.domain.dtos.request.ProductCategoryCreationRequest;
import africa.springCore.martbackend.core.portfolio.product.exception.ProductCategoryNotFoundException;
import africa.springCore.martbackend.infrastructure.exception.MapperException;
import africa.springCore.martbackend.portfolio.product.domain.dtos.response.ProductCategoryDto;
import africa.springCore.martbackend.portfolio.product.domain.dtos.response.ProductCategoryListingDto;
import africa.springCore.martbackend.portfolio.product.domain.dtos.response.ProductCategoryResponseDto;
import africa.springCore.martbackend.portfolio.product.domain.enums.ProductCategoryEnum;
import africa.springCore.martbackend.portfolio.product.domain.model.ProductCategory;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductCategoryService {
    ProductCategoryResponseDto findById(Long categoryId) throws ProductCategoryNotFoundException, MapperException;

    ProductCategoryResponseDto postAProductCategory(ProductCategoryCreationRequest productCategoryCreationRequest) throws MapperException;

    ProductCategoryResponseDto findByName(String categoryName) throws ProductCategoryNotFoundException, MapperException;

    ProductCategoryListingDto searchByName(String name, Pageable pageable) throws MapperException;
    List<ProductCategory> searchByName(String name) throws MapperException;

    List<ProductCategoryDto> getAllProductCategories(Pageable pageable);
}
