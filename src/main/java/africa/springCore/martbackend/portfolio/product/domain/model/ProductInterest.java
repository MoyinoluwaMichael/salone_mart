package africa.springCore.martbackend.portfolio.product.domain.model;

import africa.springCore.martbackend.core.base.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.math.BigDecimal;

@Entity
@Table(name = "product_interest")
@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor(staticName = "instance", access = AccessLevel.PRIVATE)
public class ProductInterest extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -7466640123337613601L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "price_interest", nullable = false)
    private Long priceInterest;

    public static ProductInterest instanceOf(boolean active, Long priceInterest) {
        return new ProductInterest(null, active, priceInterest);
    }
}
