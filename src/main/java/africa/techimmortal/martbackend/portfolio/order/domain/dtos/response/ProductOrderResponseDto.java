package africa.techimmortal.martbackend.portfolio.order.domain.dtos.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ProductOrderResponseDto {

    private Long id;

    private Long productId;

    private Long quantity;

    private String productName;

    private BigDecimal price;

    private BigDecimal totalAmount;

}
