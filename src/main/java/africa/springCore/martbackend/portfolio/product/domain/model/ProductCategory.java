package africa.springCore.martbackend.portfolio.product.domain.model;

import africa.springCore.martbackend.core.base.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;

@Entity
@Table(name = "product_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "instance", access = AccessLevel.PRIVATE)
public class ProductCategory extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -7466640123337613601L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = true, unique = true)
    private String name;

    public static ProductCategory instance(String name) {
        return new ProductCategory(null, name);
    }

}
