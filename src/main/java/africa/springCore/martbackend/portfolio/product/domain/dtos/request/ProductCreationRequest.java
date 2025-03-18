package africa.springCore.martbackend.core.portfolio.product.domain.dtos.request;

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

    @NotBlank(message = "picture is mandatory")
    private String picture;

    @NotNull(message = "categoryId is mandatory")
    private Long categoryId;

    @NotNull(message = "price is mandatory")
    private BigDecimal price;

    @NotNull(message = "quantity is mandatory")
    private Long quantity;
}
