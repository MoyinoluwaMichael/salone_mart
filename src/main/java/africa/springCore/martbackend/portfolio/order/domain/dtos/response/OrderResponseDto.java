package africa.springCore.martbackend.portfolio.order.domain.dtos.response;

import africa.springCore.martbackend.core.domain.enums.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderResponseDto {

    private Long id;

    private Long customerId;

    private List<ProductOrderResponseDto> productOrders;

    private BigDecimal deliveryFee;

    private BigDecimal totalAmount;

    private BigDecimal totalOrderAmount;

    private OrderStatus orderStatus;

    private LocalDateTime createdAt;
}
