package africa.springCore.martbackend.portfolio.order.domain.dtos.response;

import africa.springCore.martbackend.portfolio.product.domain.dtos.response.ProductResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ProductOrderResponseDto {

    private Long id;

    private ProductResponseDto product;

    private Long quantity;

    private BigDecimal price;

}
