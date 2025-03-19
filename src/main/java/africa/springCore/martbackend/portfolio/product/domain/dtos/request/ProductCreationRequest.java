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

    @NotBlank(message = "picture is mandatory")
    private String picture;

    @NotNull(message = "category is mandatory")
    private String category;

    @NotNull(message = "brand is mandatory")
    private String brand;

    @NotNull(message = "type is mandatory")
    private String type;

    @NotNull(message = "price is mandatory")
    private BigDecimal price;

    @NotNull(message = "quantity is mandatory")
    private Long quantity;

    private Long interest;
}
