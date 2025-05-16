package africa.techimmortal.martbackend.portfolio.order.domain.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ProductOrderCreationRequest {

    @NotNull(message = "productId is mandatory")
    private Long productId;

    @NotNull(message = "quantity is mandatory")
    private Long quantity;
}
