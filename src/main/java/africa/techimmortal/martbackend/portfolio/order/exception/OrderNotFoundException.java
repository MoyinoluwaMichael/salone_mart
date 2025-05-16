package africa.techimmortal.martbackend.portfolio.order.exception;

import africa.techimmortal.martbackend.infrastructure.exception.MartException;

public class OrderNotFoundException extends MartException {
    public OrderNotFoundException(String message) {
        super(message);
    }
}
