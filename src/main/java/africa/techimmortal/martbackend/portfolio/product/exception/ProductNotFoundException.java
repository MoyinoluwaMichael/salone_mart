package africa.techimmortal.martbackend.portfolio.product.exception;

import africa.techimmortal.martbackend.infrastructure.exception.MartException;

public class ProductNotFoundException extends MartException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}
