package africa.techimmortal.martbackend.core.domain.enums;

import java.util.Arrays;

public enum OrderStatus {
    PENDING, PROCESSING, IN_TRANSIT, DELIVERED, CANCELED, COMPLETED;

    public static OrderStatus parse(String orderStatus) {
        return Arrays.stream(OrderStatus.values())
                .filter(status -> status.name().equalsIgnoreCase(orderStatus))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid Order Status: " + orderStatus));
    }
}
