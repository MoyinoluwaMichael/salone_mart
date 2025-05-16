package africa.techimmortal.martbackend.portfolio.product.domain.dtos.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class ProductUpdateRequest {

    private String name;

    private String description;

    private String category;

    private BigDecimal price;

    private Long quantity;

    private Long priceDiscount;
    private Long vendorId;
}
