package africa.springCore.martbackend.core.portfolio.vendor.domain.model;

import africa.springCore.martbackend.core.base.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;

@Entity
@Table(name = "vendor_registration_document")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "instance", access = AccessLevel.PRIVATE)
public class VendorRegistrationDocument extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -7466640123337613601L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private Long vendorId;

    @Column(name = "name")
    private String name;

    @Column(name = "reference_url")
    private String referenceUrl;

}
