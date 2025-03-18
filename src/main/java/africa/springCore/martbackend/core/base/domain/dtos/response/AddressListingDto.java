package africa.springCore.martbackend.core.base.domain.dtos.response;

import africa.springCore.martbackend.common.data.BasePageableResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class AddressListingDto extends BasePageableResponse {
    private List<AddressResponseDto> addresses;
}
