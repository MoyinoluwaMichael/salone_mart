package africa.springCore.martbackend.portfolio.product.domain.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    private Long priceInterest;
    private Long vendorId;
}
