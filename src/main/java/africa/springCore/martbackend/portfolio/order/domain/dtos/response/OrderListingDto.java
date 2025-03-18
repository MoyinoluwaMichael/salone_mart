package africa.springCore.martbackend.portfolio.order.domain.dtos.response;

import africa.springCore.martbackend.common.data.BasePageableResponse;
import africa.springCore.martbackend.portfolio.order.domain.dtos.response.OrderResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class OrderListingDto extends BasePageableResponse {
    private List<OrderResponseDto> orders;
}
