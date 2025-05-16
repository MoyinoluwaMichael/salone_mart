package africa.techimmortal.martbackend.portfolio.product.exception;

import africa.techimmortal.martbackend.infrastructure.exception.MartException;

public class ProductClassificationNotFoundException extends MartException {
    public ProductClassificationNotFoundException(String message) {
        super(message);
    }
}
