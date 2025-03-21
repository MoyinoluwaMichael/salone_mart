package africa.springCore.martbackend.core.domain.dtos.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class AddressListingDto extends BasePageableResponse {
    private List<AddressResponseDto> addresses;
}
