package africa.techimmortal.martbackend.portfolio.order.domain.dtos.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class OrderCreationRequest {

    private Long customerId;
    private List<ProductOrderCreationRequest> productOrders;

}
