package africa.springCore.martbackend.portfolio.customer.domain.dtos.responses;

import africa.springCore.martbackend.core.domain.dtos.response.BioDataResponseDto;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomerResponseDto {

    private Long id;
    private BioDataResponseDto bioData;

}
