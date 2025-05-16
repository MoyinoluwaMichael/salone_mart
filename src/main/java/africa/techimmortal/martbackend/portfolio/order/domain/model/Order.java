package africa.techimmortal.martbackend.portfolio.order.domain.model;

import africa.techimmortal.martbackend.core.domain.enums.OrderStatus;
import africa.techimmortal.martbackend.core.domain.model.BaseEntity;
import africa.techimmortal.martbackend.portfolio.order.domain.dtos.request.OrderCreationRequest;
import africa.techimmortal.martbackend.portfolio.product.domain.repository.ProductRepository;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

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

    @Column(name = "tax")
    private BigDecimal tax;

    @Column(name = "total_product_amount")
    private BigDecimal totalProductAmount;

    @Column(name = "total_order_amount")
    private BigDecimal totalOrderAmount;


    @Column(name = "order_status")
    private OrderStatus orderStatus;

    public static Order mapOrderProducts(OrderCreationRequest orderCreationRequest, ProductRepository productRepository) {
        Order order = new Order();
        order.setProductOrders(
                orderCreationRequest.getProductOrders()
                        .stream()
                        .map( productOrderCreationRequest -> {
                            ProductOrder productOrder = ProductOrder.instance(productOrderCreationRequest);
                            productOrder.setVendorId(productRepository.findById(productOrderCreationRequest.getProductId()).get().getVendorId());
                            productOrder.setPrice(productRepository.findById(productOrderCreationRequest.getProductId()).get().getPrice());
                            return productOrder;
                        })
                        .collect(Collectors.toList())
        );
        return order;
    }

    public void setTotalOrderAmount() {
        this.totalOrderAmount = this.totalProductAmount.add(this.tax).add(this.deliveryFee);
    }
}
