package africa.springCore.martbackend.portfolio.customer.domain.dtos.responses;

import africa.springCore.martbackend.common.data.BasePageableResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CustomerListingDto extends BasePageableResponse {
    private List<CustomerResponseDto> customers;
}
