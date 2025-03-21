package africa.springCore.martbackend.portfolio.order.domain.repository;

import africa.springCore.martbackend.core.domain.enums.OrderStatus;
import africa.springCore.martbackend.portfolio.order.domain.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findAllByCustomerId(Long customerId, Pageable pageable);
    Page<Order> findAllByCustomerIdAndOrderStatus(Long customerId, OrderStatus orderStatus, Pageable pageable);
    Page<Order> findAllByOrderStatus(OrderStatus orderStatus, Pageable pageable);

}
