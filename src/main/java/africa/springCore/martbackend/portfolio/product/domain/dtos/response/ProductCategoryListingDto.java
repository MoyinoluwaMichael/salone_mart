package africa.springCore.martbackend.portfolio.product.domain.dtos.response;

import africa.springCore.martbackend.common.data.BasePageableResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ProductCategoryListingDto extends BasePageableResponse {
    private List<ProductCategoryResponseDto> productCategories;
}
