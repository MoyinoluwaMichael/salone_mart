package africa.springCore.martbackend.core.portfolio.product.exception;

import africa.springCore.martbackend.infrastructure.exception.MartException;

public class ProductNotFoundException extends MartException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}
