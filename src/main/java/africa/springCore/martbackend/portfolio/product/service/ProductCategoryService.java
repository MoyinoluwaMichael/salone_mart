package africa.springCore.martbackend.portfolio.product.service;

import africa.springCore.martbackend.core.domain.dtos.response.BasePageableResponse;
import africa.springCore.martbackend.infrastructure.exception.MapperException;
import africa.springCore.martbackend.portfolio.product.domain.dtos.request.ProductClassificationCreationRequest;
import africa.springCore.martbackend.portfolio.product.domain.model.ProductCategory;
import africa.springCore.martbackend.portfolio.product.exception.ProductClassificationNotFoundException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductCategoryService {
    ProductCategory findById(Long categoryId) throws MapperException, ProductClassificationNotFoundException;

    ProductCategory postAProductCategory(ProductClassificationCreationRequest productClassificationCreationRequest) throws MapperException;

    ProductCategory findByName(String categoryName) throws MapperException, ProductClassificationNotFoundException;

    BasePageableResponse<ProductCategory> searchByName(String name, Pageable pageable) throws MapperException;
    List<ProductCategory> searchByName(String name) throws MapperException;

    BasePageableResponse<ProductCategory> getAllProductCategories(Pageable pageable);
}
