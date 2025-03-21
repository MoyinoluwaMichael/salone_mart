package africa.springCore.martbackend.portfolio.product.domain.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductClassificationCreationRequest {

    @NotBlank(message = "name is mandatory")
    private String name;

    @NotBlank(message = "description is mandatory")
    private String description;
}
