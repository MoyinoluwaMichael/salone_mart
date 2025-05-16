package africa.techimmortal.martbackend.portfolio.order.exception;

import africa.techimmortal.martbackend.infrastructure.exception.MartException;

public class OrderUpdateFailedException extends MartException {
    public OrderUpdateFailedException(String message) {
        super(message);
    }
}
