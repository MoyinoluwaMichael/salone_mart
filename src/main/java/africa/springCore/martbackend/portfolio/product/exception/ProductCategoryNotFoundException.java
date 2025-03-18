package africa.springCore.martbackend.core.portfolio.product.exception;

import africa.springCore.martbackend.infrastructure.exception.MartException;

public class ProductCategoryNotFoundException extends MartException {
    public ProductCategoryNotFoundException(String message) {
        super(message);
    }
}
