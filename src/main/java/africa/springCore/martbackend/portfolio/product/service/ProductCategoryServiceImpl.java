package africa.springCore.martbackend.portfolio.product.service;

import africa.springCore.martbackend.core.domain.dtos.response.BasePageableResponse;
import africa.springCore.martbackend.core.utils.MartMapper;
import africa.springCore.martbackend.infrastructure.exception.MapperException;
import africa.springCore.martbackend.portfolio.product.domain.dtos.request.ProductClassificationCreationRequest;
import africa.springCore.martbackend.portfolio.product.domain.model.ProductCategory;
import africa.springCore.martbackend.portfolio.product.domain.repository.ProductCategoryRepository;
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
public class ProductCategoryServiceImpl implements ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;
    private final MartMapper martMapper;

    @Override
    public ProductCategory postAProductCategory(ProductClassificationCreationRequest productClassificationCreationRequest) throws MapperException {
        ProductCategory productCategory = martMapper.readValue(productClassificationCreationRequest, ProductCategory.class);
        productCategory.setName(productClassificationCreationRequest.getName().toUpperCase(Locale.ROOT));
        return productCategoryRepository.save(productCategory);
    }

    @Override
    public ProductCategory findById(Long categoryId) throws MapperException, ProductClassificationNotFoundException {
        return productCategoryRepository.findById(categoryId).orElseThrow(() -> new ProductClassificationNotFoundException(String.format(PRODUCT_CATEGORY_WITH_ID_NOT_FOUND, categoryId)));
    }

    @Override
    public ProductCategory findByName(String name) throws ProductClassificationNotFoundException {
        return productCategoryRepository.findByName(name).orElseThrow(() -> new ProductClassificationNotFoundException(String.format(PRODUCT_CATEGORY_WITH_NAME_NOT_FOUND, name)));
    }

    @Override
    public BasePageableResponse<ProductCategory> searchByName(String name, Pageable pageable) throws MapperException {
        Example<ProductCategory> example = searchCategory(name);
        Page<ProductCategory> pagedProductCategories = productCategoryRepository.findAll(example, pageable);
        return getProductCategoryListingDto(pagedProductCategories);
    }

    @Override
    public List<ProductCategory> searchByName(String name) {
        Example<ProductCategory> example = searchCategory(name);
        return productCategoryRepository.findAll(example);
    }


    private Example<ProductCategory> searchCategory(String name) {
        ExampleMatcher matcher = ExampleMatcher.matchingAll().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        ProductCategory criteria = new ProductCategory();
        criteria.setName(name);
        return Example.of(criteria, matcher);
    }

    @Override
    public BasePageableResponse<ProductCategory> getAllProductCategories(Pageable pageable) {
        Page<ProductCategory> categories = productCategoryRepository.findAll(pageable);
        return BasePageableResponse.instance(categories);
    }

    private BasePageableResponse<ProductCategory> getProductCategoryListingDto(Page<ProductCategory> pagedProductCategories) {
        Page<ProductCategory> productCategories = pagedProductCategories.map(productCategory -> productCategory);
        return BasePageableResponse.instance(productCategories);
    }
}
