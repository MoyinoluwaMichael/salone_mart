package africa.springCore.martbackend.portfolio.order.service;

import africa.springCore.martbackend.core.portfolio.order.exception.OrderCreationFailedException;
import africa.springCore.martbackend.core.portfolio.order.exception.OrderNotFoundException;
import africa.springCore.martbackend.core.portfolio.order.exception.OrderUpdateFailedException;
import africa.springCore.martbackend.core.portfolio.product.exception.ProductNotFoundException;
import africa.springCore.martbackend.infrastructure.exception.MapperException;
import africa.springCore.martbackend.infrastructure.exception.UserNotFoundException;
import africa.springCore.martbackend.portfolio.order.domain.dtos.request.OrderCreationRequest;
import africa.springCore.martbackend.portfolio.order.domain.dtos.request.ProductOrderCreationRequest;
import africa.springCore.martbackend.portfolio.order.domain.dtos.response.OrderListingDto;
import africa.springCore.martbackend.portfolio.order.domain.dtos.response.OrderResponseDto;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {
    OrderResponseDto postAnOrder(Long customerId, OrderCreationRequest orderCreationRequest) throws MapperException, ProductNotFoundException, UserNotFoundException, OrderCreationFailedException;

    OrderListingDto getAllOrders(Pageable pageable, String orderStatus);

    OrderResponseDto getOrderById(Long id) throws OrderNotFoundException, MapperException, ProductNotFoundException;

    OrderListingDto getCustomerOrders(Long customerId, String orderStatus, Pageable pageable) throws UserNotFoundException, MapperException;

    BigDecimal calculateTotalAmount(List<ProductOrderCreationRequest> productOrders, String orderType) throws MapperException, ProductNotFoundException;

    OrderResponseDto updateOrderStatus(Long orderId, String command) throws OrderNotFoundException, MapperException, ProductNotFoundException, OrderUpdateFailedException;

    Long retrieveTotalOrders();

}
