package africa.springCore.martbackend.portfolio.order.domain.model;

import africa.springCore.martbackend.core.domain.enums.OrderStatus;
import africa.springCore.martbackend.core.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "customer_order")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "instance", access = AccessLevel.PRIVATE)
public class Order extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -7466640123337613601L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ProductOrder> productOrders;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "delivery_fee")
    private BigDecimal deliveryFee;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "total_order_amount")
    private BigDecimal totalOrderAmount;


    @Column(name = "order_status")
    private OrderStatus orderStatus;

}
