package africa.springCore.martbackend.core.base.domain.dtos.request;

import africa.springCore.martbackend.common.enums.AddressType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddressCreationRequest {

    private AddressType addressType;
    private Long number;
    private String streetName;
    private String nearestBusStop;
    private String city;
    private String state;
    private String localGovernmentArea;
    private String country;
}
