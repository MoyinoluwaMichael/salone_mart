package africa.springCore.martbackend.portfolio.admin.domain.dtos.responses;

import africa.springCore.martbackend.core.domain.dtos.response.BasePageableResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class AdminListingDto extends BasePageableResponse {
    private List<AdminResponseDto> admins;
}
