package africa.springCore.martbackend.core.base.domain.dtos.response;

import africa.springCore.martbackend.common.enums.AddressType;
import africa.springCore.martbackend.common.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddressResponseDto {

    private Long id;
    private AddressType addressType;
    private int number;
    private String streetName;
    private String nearestBusStop;
    private String city;
    private String state;
    private String localGovernmentArea;
    private String country;
    private Role userType;
}
