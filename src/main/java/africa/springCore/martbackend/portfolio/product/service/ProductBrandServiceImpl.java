package africa.springCore.martbackend.portfolio.product.service;

import africa.springCore.martbackend.core.domain.dtos.response.BasePageableResponse;
import africa.springCore.martbackend.core.utils.MartMapper;
import africa.springCore.martbackend.infrastructure.exception.MapperException;
import africa.springCore.martbackend.portfolio.product.domain.dtos.request.ProductClassificationCreationRequest;
import africa.springCore.martbackend.portfolio.product.domain.model.ProductBrand;
import africa.springCore.martbackend.portfolio.product.domain.model.ProductCategory;
import africa.springCore.martbackend.portfolio.product.domain.repository.ProductBrandRepository;
import africa.springCore.martbackend.portfolio.product.exception.ProductClassificationNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

import static africa.springCore.martbackend.core.utils.Message.PRODUCT_CATEGORY_WITH_ID_NOT_FOUND;
import static africa.springCore.martbackend.core.utils.Message.PRODUCT_CATEGORY_WITH_NAME_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ProductBrandServiceImpl implements ProductBrandService {
    private final ProductBrandRepository productBrandRepository;
    private final MartMapper martMapper;

    @Override
    public ProductBrand postAProductBrand(ProductClassificationCreationRequest productClassificationCreationRequest) throws MapperException {
        ProductBrand productBrand = martMapper.readValue(productClassificationCreationRequest, ProductBrand.class);
        productBrand.setName(productClassificationCreationRequest.getName().toUpperCase(Locale.ROOT));
        return productBrandRepository.save(productBrand);
    }

    @Override
    public ProductBrand findById(Long categoryId) throws MapperException, ProductClassificationNotFoundException {
        return productBrandRepository.findById(categoryId).orElseThrow(
                        () -> new ProductClassificationNotFoundException(String.format(PRODUCT_CATEGORY_WITH_ID_NOT_FOUND, categoryId))
        );
    }

    @Override
    public ProductBrand findByName(String name) throws ProductClassificationNotFoundException {
        return productBrandRepository.findByName(name).orElseThrow(
                        () -> new ProductClassificationNotFoundException(String.format(PRODUCT_CATEGORY_WITH_NAME_NOT_FOUND, name)
        ));
    }

    @Override
    public BasePageableResponse<ProductBrand> searchByName(String name, Pageable pageable) throws MapperException {
        Example<ProductBrand> example = searchBrand(name);
        Page<ProductBrand> pagedProductBrands = productBrandRepository.findAll(example, pageable);
        return BasePageableResponse.instance(pagedProductBrands);
    }

    @Override
    public List<ProductBrand> searchByName(String name) {
        Example<ProductBrand> example = searchBrand(name);
        return productBrandRepository.findAll(example);
    }


    private Example<ProductBrand> searchBrand(String name) {
        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        ProductBrand criteria = new ProductBrand();
        criteria.setName(name);
        return Example.of(criteria, matcher);
    }

    @Override
    public BasePageableResponse<ProductBrand> getAllProductBrands(Pageable pageable) {
        Page<ProductBrand> categories = productBrandRepository.findAll(pageable);
        return BasePageableResponse.instance(categories);
    }

    private BasePageableResponse<ProductCategory> getProductCategoryListingDto(Page<ProductCategory> pagedProductCategories) {
        Page<ProductCategory> productCategories = pagedProductCategories.map(
                productCategory -> productCategory
        );
        return BasePageableResponse.instance(productCategories);
    }
}
