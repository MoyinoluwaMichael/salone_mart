package africa.springCore.martbackend.portfolio.product.exception;

import africa.springCore.martbackend.infrastructure.exception.MartException;

public class ProductClassificationNotFoundException extends MartException {
    public ProductClassificationNotFoundException(String message) {
        super(message);
    }
}
