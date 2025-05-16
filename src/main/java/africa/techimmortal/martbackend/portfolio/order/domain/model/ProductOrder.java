package africa.techimmortal.martbackend.portfolio.order.domain.model;

import africa.techimmortal.martbackend.core.domain.model.BaseEntity;
import africa.techimmortal.martbackend.portfolio.order.domain.dtos.request.ProductOrderCreationRequest;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.math.BigDecimal;

@Entity
@Table(name = "ordered_product")
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

    @Column(name = "product_name")
    private String productName;

    @Column(name = "vendor_id")
    private Long vendorId;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "amount")
    private BigDecimal totalAmount;

    public static ProductOrder instance(ProductOrderCreationRequest creationRequest) {
        ProductOrder productOrder = new ProductOrder();
        productOrder.setProductId(creationRequest.getProductId());
        productOrder.setQuantity(creationRequest.getQuantity());
        return productOrder;
    }
}
