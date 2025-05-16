package africa.techimmortal.martbackend.portfolio.product.domain.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryProductCountDto {
    private final String category;
    private final Long productCount;
}
