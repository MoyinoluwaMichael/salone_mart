package africa.springCore.martbackend.portfolio.product.domain.dtos.response;

import africa.springCore.martbackend.portfolio.product.domain.model.ProductCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryProductCountDto {
    private final ProductCategory category;
    private final Long productCount;
}
