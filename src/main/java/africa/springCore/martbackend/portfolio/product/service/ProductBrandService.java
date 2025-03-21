package africa.springCore.martbackend.portfolio.product.service;

import africa.springCore.martbackend.core.domain.dtos.response.BasePageableResponse;
import africa.springCore.martbackend.infrastructure.exception.MapperException;
import africa.springCore.martbackend.portfolio.product.domain.dtos.request.ProductClassificationCreationRequest;
import africa.springCore.martbackend.portfolio.product.domain.model.ProductBrand;
import africa.springCore.martbackend.portfolio.product.exception.ProductClassificationNotFoundException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductBrandService {
    ProductBrand findById(Long categoryId) throws MapperException, ProductClassificationNotFoundException;

    ProductBrand postAProductBrand(ProductClassificationCreationRequest productClassificationCreationRequest) throws MapperException;

    ProductBrand findByName(String categoryName) throws MapperException, ProductClassificationNotFoundException;

    BasePageableResponse<ProductBrand> searchByName(String name, Pageable pageable) throws MapperException;
    List<ProductBrand> searchByName(String name) throws MapperException;

    BasePageableResponse<ProductBrand> getAllProductBrands(Pageable pageable);
}
