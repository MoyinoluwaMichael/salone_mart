package africa.springCore.martbackend.portfolio.product.service;

import africa.springCore.martbackend.core.domain.dtos.response.BasePageableResponse;
import africa.springCore.martbackend.core.domain.enums.ApprovalStatus;
import africa.springCore.martbackend.core.portfolio.product.exception.ProductNotFoundException;
import africa.springCore.martbackend.infrastructure.exception.MapperException;
import africa.springCore.martbackend.infrastructure.exception.UserNotFoundException;
import africa.springCore.martbackend.portfolio.product.domain.dtos.request.ProductCreationRequest;
import africa.springCore.martbackend.portfolio.product.domain.model.Product;
import africa.springCore.martbackend.portfolio.product.domain.model.ProductBrand;
import africa.springCore.martbackend.portfolio.product.domain.model.ProductCategory;
import africa.springCore.martbackend.portfolio.product.domain.repository.ProductRepository;
import africa.springCore.martbackend.portfolio.product.exception.ProductClassificationNotFoundException;
import africa.springCore.martbackend.portfolio.product.exception.ProductCreationFailedException;
import africa.springCore.martbackend.portfolio.vendor.domain.dtos.responses.VendorResponseDto;
import africa.springCore.martbackend.portfolio.vendor.service.VendorService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static africa.springCore.martbackend.core.utils.Message.PRODUCT_WITH_ID_NOT_FOUND;
import static africa.springCore.martbackend.core.utils.AppUtils.CATEGORY_NAME;
import static africa.springCore.martbackend.core.utils.AppUtils.PRODUCT_NAME;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryService productCategoryService;
    private final ProductBrandService productBrandService;
    private final VendorService vendorService;

    @Override
    @Transactional
    public Product postAProduct(Long vendorId, ProductCreationRequest productCreationRequest) throws UserNotFoundException, ProductCreationFailedException, ProductClassificationNotFoundException, MapperException {
        VendorResponseDto vendorResponseDto = vendorService.findById(vendorId);
        if (vendorResponseDto.getStatus() != ApprovalStatus.APPROVED) {
            throw new ProductCreationFailedException("Vendor with id " + vendorId + " is not in approved state");
        }
        if (productRepository.findByVendorIdAndNameAndCategory_IdAndBrand_Id(vendorId, productCreationRequest.getName(), productCreationRequest.getCategoryId(), productCreationRequest.getBrandId()).isPresent()) {
            throw new ProductCreationFailedException("Vendor with id " + vendorId + " already created product with name: " + productCreationRequest.getName());
        }
        if (productCreationRequest.getQuantity() < 1) {
            throw new ProductCreationFailedException("Product quantity must be at least one");
        }
        if (productCreationRequest.getPrice().compareTo(BigDecimal.TEN) < 0) {
            throw new ProductCreationFailedException("Price cannot be less than ten");
        }

        // Ensure ProductCategory and ProductBrand are managed by the current persistence context
        ProductCategory productCategory = productCategoryService.findById(productCreationRequest.getCategoryId());
        ProductBrand productBrand = productBrandService.findById(productCreationRequest.getBrandId());

        Product product = new Product();
        product.setName(productCreationRequest.getName());
        product.setDescription(productCreationRequest.getDescription());
        product.setQuantity(productCreationRequest.getQuantity());
        product.setPrice(productCreationRequest.getPrice());
        product.setCategory(productCategory);
        product.setBrand(productBrand);
        product.setVendorId(vendorId);
        if (productCreationRequest.getInterest() != null && productCreationRequest.getInterest() > 0) {
            product.setInterest(productCreationRequest.getInterest());
            product.setDiscountedPrice(getDiscountedPrice(productCreationRequest));
        }
        return productRepository.save(product);
    }

    private BigDecimal getDiscountedPrice(ProductCreationRequest request) {
        BigDecimal interestInPercentage = BigDecimal.valueOf(request.getInterest());
        BigDecimal interestInDecimal = interestInPercentage.divide(BigDecimal.valueOf(100), 3, RoundingMode.HALF_UP);
        BigDecimal priceInterest = request.getPrice().multiply(interestInDecimal).setScale(3, RoundingMode.HALF_UP);
        return request.getPrice().subtract(priceInterest).setScale(0, RoundingMode.HALF_UP);
    }

    private ProductCategory findProductCategoryById(Long categoryId) throws MapperException, ProductClassificationNotFoundException {
        return productCategoryService.findById(categoryId);
    }

    private ProductCategory findProductCategoryByName(String categoryName) throws MapperException, ProductClassificationNotFoundException {
        return productCategoryService.findByName(categoryName);
    }

    @Override
    public BasePageableResponse<Product> getAllProducts(Pageable pageable) {
        Page<Product> pagedProducts = productRepository.findAll(pageable);
        return BasePageableResponse.instance(pagedProducts);
    }

    @Override
    public Product getProductById(Long id) throws ProductNotFoundException {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(String.format(PRODUCT_WITH_ID_NOT_FOUND, id)));
    }

    @Override
    public BasePageableResponse<Product> getVendorProducts(Long vendorId, Pageable pageable) {
        Page<Product> pagedProducts = productRepository.findByVendorId(vendorId, pageable);
        return BasePageableResponse.instance(pagedProducts);
    }

    @Override
    public BasePageableResponse<Product> searchProducts(String searchParam, String value, Pageable pageable) throws MapperException {
        ExampleMatcher matcher = ExampleMatcher.matchingAll().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Product criteria = new Product();
        if (searchParam.equals(PRODUCT_NAME)) {
            criteria.setName(value);
        } else if (searchParam.equals(CATEGORY_NAME)) {
            List<ProductCategory> productCategories = productCategoryService.searchByName(value);
            if (!productCategories.isEmpty()) {
                criteria.setCategory(productCategories.get(0));
            }
        }
        Example<Product> example = Example.of(criteria, matcher);
        Page<Product> pagedProducts = productRepository.findAll(example, pageable);
        return BasePageableResponse.instance(pagedProducts);
    }

    @Override
    public Long retrieveTotalProducts() {
        return productRepository.count();
    }
}
