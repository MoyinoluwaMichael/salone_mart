package africa.springCore.martbackend.portfolio.product.domain.model;

import africa.springCore.martbackend.core.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;

@Entity
@Table(name = "product_discount")
@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor(staticName = "instance", access = AccessLevel.PRIVATE)
public class ProductDiscount extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -7466640123337613601L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "price_discount", nullable = false)
    private Long priceDiscount;

    public static ProductDiscount instanceOf(boolean active, Long priceInterest) {
        return new ProductDiscount(null, active, priceInterest);
    }
}
