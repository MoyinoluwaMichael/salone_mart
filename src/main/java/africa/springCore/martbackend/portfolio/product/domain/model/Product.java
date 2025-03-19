package africa.springCore.martbackend.portfolio.product.domain.model;

import africa.springCore.martbackend.core.base.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "picture", nullable = false)
    private String picture;

    @ManyToOne(fetch = FetchType.EAGER)
    private ProductCategory category;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "discounted_price")
    private BigDecimal discountedPrice;

    @Column(name = "quantity", nullable = false)
    private Long quantity;

    @JoinColumn(name = "interest", nullable = false)
    @OneToOne(cascade = CascadeType.ALL)
    private ProductInterest interest;

    public void setInterest(Long interest) {
        this.interest = ProductInterest.instanceOf( true, interest);
    }
}
