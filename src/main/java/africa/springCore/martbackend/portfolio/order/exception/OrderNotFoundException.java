package africa.springCore.martbackend.core.portfolio.order.exception;

import africa.springCore.martbackend.infrastructure.exception.MartException;

public class OrderNotFoundException extends MartException {
    public OrderNotFoundException(String message) {
        super(message);
    }
}
