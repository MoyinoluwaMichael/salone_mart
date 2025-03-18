package africa.springCore.martbackend.portfolio.product.domain.dtos.response;

import africa.springCore.martbackend.common.data.BasePageableResponse;
import africa.springCore.martbackend.portfolio.product.domain.dtos.response.ProductResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ProductListingDto extends BasePageableResponse {
    private List<ProductResponseDto> products;
}
