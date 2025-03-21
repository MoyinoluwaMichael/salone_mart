package africa.springCore.martbackend.core.domain.dtos.response;

import africa.springCore.martbackend.core.domain.enums.AddressType;
import africa.springCore.martbackend.core.domain.enums.Role;
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
