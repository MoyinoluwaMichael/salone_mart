package africa.springCore.martbackend.core.portfolio.order.exception;

import africa.springCore.martbackend.infrastructure.exception.MartException;

public class OrderUpdateFailedException extends MartException {
    public OrderUpdateFailedException(String message) {
        super(message);
    }
}
