package africa.techimmortal.martbackend.portfolio.customer.domain.dtos.responses;

import africa.techimmortal.martbackend.core.domain.dtos.response.BasePageableResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CustomerListingDto extends BasePageableResponse {
    private List<CustomerResponseDto> customers;
}
