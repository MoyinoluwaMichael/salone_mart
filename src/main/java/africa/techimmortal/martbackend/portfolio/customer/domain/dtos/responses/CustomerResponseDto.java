package africa.techimmortal.martbackend.portfolio.customer.domain.dtos.responses;

import africa.techimmortal.martbackend.core.domain.dtos.response.BioDataResponseDto;
import africa.techimmortal.martbackend.portfolio.customer.domain.model.Customer;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomerResponseDto {

    private Long id;
    private BioDataResponseDto bioData;

    public static CustomerResponseDto parse(Customer foundCustomer) {
        CustomerResponseDto customerResponseDto = new CustomerResponseDto();
        customerResponseDto.setId(foundCustomer.getId());
        customerResponseDto.setBioData(BioDataResponseDto.parse(foundCustomer.getBioData()));
        return customerResponseDto;
    }
}
