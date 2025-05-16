package africa.springCore.martbackend.portfolio.order.domain.dtos.response;

import africa.springCore.martbackend.core.domain.enums.OrderStatus;
import africa.springCore.martbackend.portfolio.order.domain.model.ProductOrder;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
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

    private BigDecimal totalOrderAmount;

    private OrderStatus orderStatus;

    private BigDecimal tax;

    private BigDecimal totalProductAmount;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime createdAt;

}
