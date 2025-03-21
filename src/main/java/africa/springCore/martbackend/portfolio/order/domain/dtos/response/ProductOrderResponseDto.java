package africa.springCore.martbackend.portfolio.order.domain.dtos.response;

import africa.springCore.martbackend.portfolio.product.domain.model.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ProductOrderResponseDto {

    private Long id;

    private Product product;

    private Long quantity;

    private BigDecimal price;

}
