package africa.springCore.martbackend.portfolio.order.domain.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
public class OrderCreationRequest {

    @NotNull(message = "At least one order is required")
    private List<ProductOrderCreationRequest> productOrders;

    @NotNull(message = "Delivery fee is mandatory")
    private BigDecimal deliveryFee;

}
