package africa.springCore.martbackend.portfolio.vendor.domain.model;

import africa.springCore.martbackend.common.enums.ApprovalStatus;
import africa.springCore.martbackend.core.base.domain.model.BaseEntity;
import africa.springCore.martbackend.core.base.domain.model.BioData;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;

@Entity
@Table(name = "vendor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "instance", access = AccessLevel.PRIVATE)
public class Vendor extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -7466640123337613601L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "business_name")
    private String businessName;

    @Column(name = "status")
    private ApprovalStatus approvalStatus;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private BioData bioData;


}
