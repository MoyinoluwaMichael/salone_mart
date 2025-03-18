package africa.springCore.martbackend.portfolio.product.service;

import africa.springCore.martbackend.common.enums.ApprovalStatus;
import africa.springCore.martbackend.common.utils.MartMapper;
import africa.springCore.martbackend.core.portfolio.product.domain.dtos.request.ProductCreationRequest;
import africa.springCore.martbackend.core.portfolio.product.domain.model.Product;
import africa.springCore.martbackend.core.portfolio.product.exception.ProductCategoryNotFoundException;
import africa.springCore.martbackend.core.portfolio.product.exception.ProductCreationFailedException;
import africa.springCore.martbackend.core.portfolio.product.exception.ProductNotFoundException;
import africa.springCore.martbackend.infrastructure.configuration.ApplicationProperty;
import africa.springCore.martbackend.infrastructure.exception.MapperException;
import africa.springCore.martbackend.infrastructure.exception.UserNotFoundException;
import africa.springCore.martbackend.portfolio.product.domain.dtos.response.ProductCategoryListingDto;
import africa.springCore.martbackend.portfolio.product.domain.dtos.response.ProductCategoryResponseDto;
import africa.springCore.martbackend.portfolio.product.domain.dtos.response.ProductListingDto;
import africa.springCore.martbackend.portfolio.product.domain.dtos.response.ProductResponseDto;
import africa.springCore.martbackend.portfolio.product.domain.repository.ProductRepository;
import africa.springCore.martbackend.portfolio.vendor.domain.dtos.responses.VendorResponseDto;
import africa.springCore.martbackend.portfolio.vendor.service.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static africa.springCore.martbackend.common.Message.PRODUCT_WITH_ID_NOT_FOUND;
import static africa.springCore.martbackend.common.utils.AppUtils.CATEGORY_NAME;
import static africa.springCore.martbackend.common.utils.AppUtils.PRODUCT_NAME;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final MartMapper martMapper;
    private final ProductRepository productRepository;
    private final ProductCategoryService productCategoryService;
    private final VendorService vendorService;
    private final ApplicationProperty applicationProperty;


    @Override
    public ProductResponseDto postAProduct(Long vendorId, ProductCreationRequest productCreationRequest) throws UserNotFoundException, MapperException, ProductCategoryNotFoundException, ProductCreationFailedException {
        VendorResponseDto vendorResponseDto = vendorService.findById(vendorId);
        if (vendorResponseDto.getApprovalStatus() != ApprovalStatus.APPROVED) {
            throw new ProductCreationFailedException("Vendor with id " + vendorId + " is not in approved state");
        }
        if (productRepository.findByVendorIdAndNameAndCategoryId(vendorId, productCreationRequest.getName(), productCreationRequest.getCategoryId()).isPresent()) {
            throw new ProductCreationFailedException("Vendor with id " + vendorId + " already created product with name: " + productCreationRequest.getName() + " and category Id " + productCreationRequest.getCategoryId());
        }
        findProductCategoryById(productCreationRequest.getCategoryId());
        if (productCreationRequest.getQuantity() < 1) {
            throw new ProductCreationFailedException("Product quantity must be at least one");
        }
        if (productCreationRequest.getPrice().compareTo(BigDecimal.TEN) < 0) {
            throw new ProductCreationFailedException("Price cannot be less than ten");
        }
        Product product = martMapper.readValue(productCreationRequest, Product.class);
        product.setPrice(getSystemPrice(productCreationRequest.getPrice()));
        product.setCategoryId(productCreationRequest.getCategoryId());
        product.setVendorId(vendorId);
        product.setPriceInterestInPercentage(applicationProperty.getPriceInterest());
        return getProductResponseDto(productRepository.save(product));
    }

    private BigDecimal getSystemPrice(BigDecimal price) {
        BigDecimal interestInPercentage = applicationProperty.getPriceInterest();
        BigDecimal interestInDecimal = interestInPercentage.divide(BigDecimal.valueOf(100), 3, RoundingMode.HALF_UP);
        BigDecimal priceInterest = price.multiply(interestInDecimal).setScale(3, RoundingMode.HALF_UP);
        return price.add(priceInterest).setScale(0, RoundingMode.HALF_UP);
    }

    private ProductCategoryResponseDto findProductCategoryById(Long categoryId) throws ProductCategoryNotFoundException, MapperException {
        return productCategoryService.findById(categoryId);
    }

    private ProductCategoryResponseDto findProductCategoryByName(String categoryName) throws ProductCategoryNotFoundException, MapperException {
        return productCategoryService.findByName(categoryName);
    }

    private ProductResponseDto getProductResponseDto(Product product) throws MapperException, ProductCategoryNotFoundException {
        ProductCategoryResponseDto productCategoryResponseDto = findProductCategoryById(product.getCategoryId());
        ProductResponseDto productResponseDto = martMapper.readValue(product, ProductResponseDto.class);
        productResponseDto.setCategory(productCategoryResponseDto);
        return productResponseDto;
    }

    @Override
    public ProductListingDto getAllProducts(Pageable pageable) {
        Page<Product> pagedProducts = productRepository.findAll(pageable);
        return getProductListingDto(pagedProducts);
    }

    @Override
    public ProductResponseDto getProductById(Long id) throws ProductNotFoundException, MapperException, ProductCategoryNotFoundException {
        return getProductResponseDto(productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(String.format(PRODUCT_WITH_ID_NOT_FOUND, id))));
    }

    @Override
    public ProductListingDto getVendorProducts(Long vendorId, Pageable pageable) {
        Page<Product> pagedProducts = productRepository.findByVendorId(vendorId, pageable);
        return getProductListingDto(pagedProducts);
    }

    @Override
    public ProductListingDto searchProducts(String searchParam, String value, Pageable pageable) throws ProductCategoryNotFoundException, MapperException {
        ExampleMatcher matcher = ExampleMatcher.matchingAll().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Product criteria = new Product();
        if (searchParam.equals(PRODUCT_NAME)) {
            criteria.setName(value);
        } else if (searchParam.equals(CATEGORY_NAME)) {
            ProductCategoryListingDto productCategoryListingDto = productCategoryService.searchByName(value, pageable);
            if (productCategoryListingDto.getProductCategories().size() > 0) {
                criteria.setCategoryId(productCategoryListingDto.getProductCategories().get(0).getId());
            }
        }
        Example<Product> example = Example.of(criteria, matcher);
        Page<Product> pagedProducts = productRepository.findAll(example, pageable);
        return getProductListingDto(pagedProducts);
    }

    private ProductListingDto getProductListingDto(Page<Product> pagedProducts) {
        Page<ProductResponseDto> products = pagedProducts.map(product -> {
            try {
                return getProductResponseDto(product);
            } catch (MapperException | ProductCategoryNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        });
        ProductListingDto productListingDto = new ProductListingDto();
        productListingDto.setProducts(products.getContent());
        productListingDto.setPageNumber(products.getNumber());
        productListingDto.setPageSize(products.getSize());
        return productListingDto;
    }
}
