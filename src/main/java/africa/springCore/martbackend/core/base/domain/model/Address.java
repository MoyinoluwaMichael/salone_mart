package africa.springCore.martbackend.core.base.domain.model;

import africa.springCore.martbackend.common.enums.AddressType;
import africa.springCore.martbackend.common.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter

@Table(name = "address")
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Entity
public class Address extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long userId;
    private AddressType addressType;
    private Long number;
    private String streetName;
    private String nearestBusStop;
    private String city;
    private String state;
    private String localGovernmentArea;
    private String country;

    @Column(name = "user_type", nullable = false)
    private Role userType;
}
