package africa.springCore.martbackend.portfolio.admin.domain.dtos.responses;

import africa.springCore.martbackend.core.base.domain.dtos.response.BioDataResponseDto;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AdminResponseDto {

    private Long id;
    private BioDataResponseDto bioData;
}
