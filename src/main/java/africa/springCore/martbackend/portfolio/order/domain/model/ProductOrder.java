package africa.springCore.martbackend.portfolio.order.domain.model;

import africa.springCore.martbackend.core.domain.model.BaseEntity;
import africa.springCore.martbackend.portfolio.order.domain.dtos.request.ProductOrderCreationRequest;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.util.List;

@Entity
@Table(name = "product_order")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "instance", access = AccessLevel.PRIVATE)
public class ProductOrder extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -7466640123337613601L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "quantity", nullable = true)
    private Long quantity;

    public static ProductOrder instance(ProductOrderCreationRequest productOrders) {
        ProductOrder productOrder = new ProductOrder();
        productOrder.setProductId(productOrders.getProductId());
        productOrder.setQuantity(productOrders.getQuantity());
        return productOrder;
    }
}
