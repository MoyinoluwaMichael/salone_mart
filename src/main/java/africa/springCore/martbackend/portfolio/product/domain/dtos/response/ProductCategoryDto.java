package africa.springCore.martbackend.portfolio.product.domain.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ProductCategoryDto {
    private String name;
    private String description;
    private List<String> brands;
    private List<String> productTypes;
}
