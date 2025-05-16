package africa.springCore.martbackend.portfolio.product.service;

import africa.springCore.martbackend.core.domain.dtos.response.BasePageableResponse;
import africa.springCore.martbackend.core.domain.enums.ApprovalStatus;
import africa.springCore.martbackend.core.portfolio.product.exception.ProductNotFoundException;
import africa.springCore.martbackend.infrastructure.exception.EntityValidationException;
import africa.springCore.martbackend.portfolio.product.domain.dtos.request.ProductUpdateRequest;
import africa.springCore.martbackend.portfolio.system.service.CodeService;
import africa.springCore.martbackend.infrastructure.exception.MapperException;
import africa.springCore.martbackend.infrastructure.exception.UserNotFoundException;
import africa.springCore.martbackend.portfolio.product.domain.dtos.request.ProductCreationRequest;
import africa.springCore.martbackend.portfolio.product.domain.model.Product;
import africa.springCore.martbackend.portfolio.product.domain.repository.ProductRepository;
import africa.springCore.martbackend.portfolio.product.exception.ProductClassificationNotFoundException;
import africa.springCore.martbackend.portfolio.product.exception.ProductCreationFailedException;
import africa.springCore.martbackend.portfolio.vendor.domain.dtos.responses.VendorResponseDto;
import africa.springCore.martbackend.portfolio.vendor.service.VendorService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static africa.springCore.martbackend.core.utils.AppUtils.CATEGORY_NAME;
import static africa.springCore.martbackend.core.utils.AppUtils.PRODUCT_NAME;
import static africa.springCore.martbackend.core.utils.Message.*;
import static africa.springCore.martbackend.portfolio.system.service.CodeServiceImpl.PRODUCT_CATEGORY;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CodeService codeService;
    private final VendorService vendorService;

    @Override
    @Transactional
    public Product postAProduct(ProductCreationRequest productCreationRequest) throws UserNotFoundException, ProductCreationFailedException, ProductClassificationNotFoundException, MapperException {
        Long vendorId = productCreationRequest.getVendorId();
        codeService.retrieveAllCodeValuesByCodeNameAndCodeValueName(PRODUCT_CATEGORY, productCreationRequest.getCategory());
        VendorResponseDto vendorResponseDto = vendorService.findById(vendorId);
        if (vendorResponseDto.getStatus() != ApprovalStatus.APPROVED) {
            throw new ProductCreationFailedException("Vendor with id " + vendorId + " is not in approved state");
        }
        if (productRepository.findByVendorIdAndNameAndCategory(vendorId, productCreationRequest.getName(), productCreationRequest.getCategory()).isPresent()) {
            throw new ProductCreationFailedException("Vendor with id " + vendorId + " already created product with name: " + productCreationRequest.getName());
        }
        if (productCreationRequest.getQuantity() < 1) {
            throw new ProductCreationFailedException("Product quantity must be at least one");
        }
        if (productCreationRequest.getPrice().compareTo(BigDecimal.ONE) < 0) {
            throw new ProductCreationFailedException("Price cannot be less than ten");
        }

        Product product = new Product();
        product.setName(productCreationRequest.getName());
        product.setDescription(productCreationRequest.getDescription());
        product.setQuantity(productCreationRequest.getQuantity());
        product.setPrice(productCreationRequest.getPrice());
        product.setCategory(productCreationRequest.getCategory());
        product.setVendorId(vendorId);
        if (productCreationRequest.getDiscount() != null && productCreationRequest.getDiscount() > 0) {
            product.setDiscount(productCreationRequest.getDiscount());
            product.setDiscountedPrice(getDiscountedPrice(productCreationRequest.getPrice(), productCreationRequest.getDiscount()));
        }
        return productRepository.save(product);
    }

    private BigDecimal getDiscountedPrice(BigDecimal price, Long interest) {
        BigDecimal interestInPercentage = BigDecimal.valueOf(interest);
        BigDecimal interestInDecimal = interestInPercentage.divide(BigDecimal.valueOf(100), 3, RoundingMode.HALF_UP);
        BigDecimal priceDiscount = price.multiply(interestInDecimal).setScale(3, RoundingMode.HALF_UP);
        return price.subtract(priceDiscount).setScale(0, RoundingMode.HALF_UP);
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
            criteria.setCategory(value);
        }
        Example<Product> example = Example.of(criteria, matcher);
        Page<Product> pagedProducts = productRepository.findAll(example, pageable);
        return BasePageableResponse.instance(pagedProducts);
    }

    @Override
    public Long retrieveTotalProducts() {
        return productRepository.count();
    }

    @Override
    public Product updateProductById(Long id, ProductUpdateRequest productUpdateRequest) throws ProductNotFoundException, UserNotFoundException, MapperException, EntityValidationException {
        Product product = getProductById(id);

        Long vendorId = productUpdateRequest.getVendorId();
        if (vendorId != null) {
            vendorService.findById(vendorId);
            if (!vendorId.equals(product.getVendorId())){
                throw new ProductNotFoundException(String.format(PRODUCT_WITH_ID_NOT_OWNED_BY_VENDOR, product.getId(), vendorId));
            }
        }else {
            throw new EntityValidationException("Vendor ID cannot be null");
        }

        if (productUpdateRequest.getName() != null) {
            product.setName(productUpdateRequest.getName());
        }
        if (productUpdateRequest.getDescription() != null) {
            product.setDescription(productUpdateRequest.getDescription());
        }
        if (StringUtils.isNotBlank(productUpdateRequest.getCategory())) {
            product.setCategory(productUpdateRequest.getCategory());
        }
        if (productUpdateRequest.getPrice() != null && productUpdateRequest.getPrice().compareTo(BigDecimal.ZERO) > 0) {
            product.setPrice(productUpdateRequest.getPrice());
        }
        if (productUpdateRequest.getQuantity() != null && productUpdateRequest.getQuantity() > 0) {
            product.setQuantity(productUpdateRequest.getQuantity());
        }
        if (productUpdateRequest.getPriceDiscount() != null && productUpdateRequest.getPriceDiscount() > 0) {
            product.setDiscount(productUpdateRequest.getPriceDiscount());
            product.setDiscountedPrice(getDiscountedPrice(product.getPrice(), productUpdateRequest.getPriceDiscount()));
        }

        return productRepository.save(product);
    }
}
