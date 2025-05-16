package africa.springCore.martbackend.portfolio.product.domain.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class ProductCreationRequest {

    @NotBlank(message = "name is mandatory")
    private String name;

    @NotBlank(message = "description is mandatory")
    private String description;

    @NotBlank(message = "categoryId is mandatory")
    private String category;

    @NotNull(message = "price is mandatory")
    private BigDecimal price;

    @NotNull(message = "quantity is mandatory")
    private Long quantity;

    @NotNull(message = "vendorId is mandatory")
    private Long vendorId;

    private Long discount;
}
