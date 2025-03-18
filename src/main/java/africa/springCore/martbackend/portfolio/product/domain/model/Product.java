package africa.springCore.martbackend.core.portfolio.product.domain.model;

import africa.springCore.martbackend.core.base.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.math.BigDecimal;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor(staticName = "instance", access = AccessLevel.PRIVATE)
public class Product extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -7466640123337613601L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "vendor_id")
    private Long vendorId;

    @Column(name = "name", nullable = true)
    private String name;

    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "picture", nullable = true)
    private String picture;

    @Column(name = "category", nullable = true)
    private Long categoryId;

    @Column(name = "price", nullable = true)
    private BigDecimal price;

    @Column(name = "quantity", nullable = true)
    private Long quantity;

    @Column(name = "price_interest", nullable = false)
    private BigDecimal priceInterestInPercentage;
}
