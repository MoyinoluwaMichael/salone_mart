package africa.springCore.martbackend.portfolio.order.domain.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
public class OrderCreationRequest {

    private List<ProductOrderCreationRequest> productOrders;

}
