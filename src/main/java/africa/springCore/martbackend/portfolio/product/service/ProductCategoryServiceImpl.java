package africa.springCore.martbackend.portfolio.product.service;

import africa.springCore.martbackend.common.utils.MartMapper;
import africa.springCore.martbackend.core.portfolio.product.domain.dtos.request.ProductCategoryCreationRequest;
import africa.springCore.martbackend.core.portfolio.product.exception.ProductCategoryNotFoundException;
import africa.springCore.martbackend.infrastructure.exception.MapperException;
import africa.springCore.martbackend.portfolio.product.domain.dtos.response.ProductCategoryDto;
import africa.springCore.martbackend.portfolio.product.domain.dtos.response.ProductCategoryListingDto;
import africa.springCore.martbackend.portfolio.product.domain.dtos.response.ProductCategoryResponseDto;
import africa.springCore.martbackend.portfolio.product.domain.enums.ProductCategoryEnum;
import africa.springCore.martbackend.portfolio.product.domain.model.ProductCategory;
import africa.springCore.martbackend.portfolio.product.domain.repository.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static africa.springCore.martbackend.common.Message.PRODUCT_CATEGORY_WITH_ID_NOT_FOUND;
import static africa.springCore.martbackend.common.Message.PRODUCT_CATEGORY_WITH_NAME_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ProductCategoryServiceImpl implements ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;
    private final MartMapper martMapper;

    @Override
    public ProductCategoryResponseDto postAProductCategory(ProductCategoryCreationRequest productCategoryCreationRequest) throws MapperException {
        ProductCategory productCategory = martMapper.readValue(productCategoryCreationRequest, ProductCategory.class);
        productCategory.setName(productCategoryCreationRequest.getName().toUpperCase(Locale.ROOT));
        return getProductCategoryResponseDto(
                productCategoryRepository.save(productCategory)
        );
    }

    @Override
    public ProductCategoryResponseDto findById(Long categoryId) throws ProductCategoryNotFoundException, MapperException {
        return getProductCategoryResponseDto(
                productCategoryRepository.findById(categoryId).orElseThrow(
                        () -> new ProductCategoryNotFoundException(String.format(PRODUCT_CATEGORY_WITH_ID_NOT_FOUND, categoryId))
                )
        );
    }

    @Override
    public ProductCategoryResponseDto findByName(String name) throws ProductCategoryNotFoundException, MapperException {
        return getProductCategoryResponseDto(
                productCategoryRepository.findByName(name).orElseThrow(
                        () -> new ProductCategoryNotFoundException(String.format(PRODUCT_CATEGORY_WITH_NAME_NOT_FOUND, name))
                )
        );
    }

    @Override
    public ProductCategoryListingDto searchByName(String name, Pageable pageable) throws MapperException {
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
        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        ProductCategory criteria = new ProductCategory();
        criteria.setName(name);
        return Example.of(criteria, matcher);
    }

    @Override
    public List<ProductCategoryDto> getAllProductCategories(Pageable pageable) {
        return Arrays.stream(ProductCategoryEnum.values())
                .map(category -> new ProductCategoryDto(
                        category.name(),
                        category.getDescription(),
                        category.getBrands(),
                        category.getProductTypes()
                ))
                .toList();
    }

    private ProductCategoryListingDto getProductCategoryListingDto(Page<ProductCategory> pagedProductCategories) {
        Page<ProductCategoryResponseDto> productCategories = pagedProductCategories.map(
                productCategory -> {
                    try {
                        return getProductCategoryResponseDto(productCategory);
                    } catch (MapperException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
        );
        ProductCategoryListingDto productCategoryListingDto = new ProductCategoryListingDto();
        productCategoryListingDto.setProductCategories(productCategories.getContent());
        productCategoryListingDto.setPageNumber(productCategories.getNumber());
        productCategoryListingDto.setPageSize(productCategories.getSize());
        return productCategoryListingDto;
    }

    private ProductCategoryResponseDto getProductCategoryResponseDto(ProductCategory productCategory) throws MapperException {
        return martMapper.readValue(productCategory, ProductCategoryResponseDto.class);
    }
}
