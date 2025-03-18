package africa.springCore.martbackend.core.portfolio.order.exception;

import africa.springCore.martbackend.infrastructure.exception.MartException;

public class OrderCreationFailedException extends MartException {
    public OrderCreationFailedException(String message) {
        super(message);
    }
}
