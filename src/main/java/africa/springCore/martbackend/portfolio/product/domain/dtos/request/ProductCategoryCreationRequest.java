package africa.springCore.martbackend.core.portfolio.product.domain.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductCategoryCreationRequest {

    @NotBlank(message = "name is mandatory")
    private String name;
}
