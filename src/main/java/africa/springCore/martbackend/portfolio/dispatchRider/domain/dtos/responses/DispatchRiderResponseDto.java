package africa.springCore.martbackend.portfolio.dispatchRider.domain.dtos.responses;

import africa.springCore.martbackend.core.domain.dtos.response.BioDataResponseDto;
import africa.springCore.martbackend.core.portfolio.dispatchRider.domain.model.DispatchRider;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DispatchRiderResponseDto {

    private Long id;
    private BioDataResponseDto bioData;

    public static DispatchRiderResponseDto parse(DispatchRider foundRider) {
        DispatchRiderResponseDto dispatchRiderResponseDto = new DispatchRiderResponseDto();
        dispatchRiderResponseDto.setId(foundRider.getId());
        dispatchRiderResponseDto.setBioData(BioDataResponseDto.parse(foundRider.getBioData()));
        return dispatchRiderResponseDto;
    }
}
