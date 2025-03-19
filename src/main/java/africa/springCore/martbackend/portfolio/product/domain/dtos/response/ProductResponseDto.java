package africa.springCore.martbackend.portfolio.product.domain.dtos.response;

import africa.springCore.martbackend.portfolio.product.domain.dtos.response.ProductCategoryResponseDto;
import africa.springCore.martbackend.portfolio.product.domain.model.ProductInterest;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ProductResponseDto {

    private Long id;

    private Long vendorId;

    private String name;

    private String description;

    private String picture;

    private ProductCategoryResponseDto category;

    private BigDecimal price;

    private Long quantity;

    private BigDecimal discountedPrice;

    private ProductInterest interest;
}
