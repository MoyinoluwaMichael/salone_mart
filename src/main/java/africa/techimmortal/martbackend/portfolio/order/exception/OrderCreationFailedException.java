package africa.techimmortal.martbackend.portfolio.order.exception;

import africa.techimmortal.martbackend.infrastructure.exception.MartException;

public class OrderCreationFailedException extends MartException {
    public OrderCreationFailedException(String message) {
        super(message);
    }
}
