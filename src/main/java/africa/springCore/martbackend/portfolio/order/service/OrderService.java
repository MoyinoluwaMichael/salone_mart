package africa.springCore.martbackend.portfolio.order.service;

import africa.springCore.martbackend.core.domain.dtos.response.BasePageableResponse;
import africa.springCore.martbackend.core.portfolio.order.exception.OrderCreationFailedException;
import africa.springCore.martbackend.core.portfolio.order.exception.OrderNotFoundException;
import africa.springCore.martbackend.core.portfolio.order.exception.OrderUpdateFailedException;
import africa.springCore.martbackend.core.portfolio.product.exception.ProductNotFoundException;
import africa.springCore.martbackend.infrastructure.exception.MapperException;
import africa.springCore.martbackend.infrastructure.exception.UserNotFoundException;
import africa.springCore.martbackend.portfolio.order.domain.dtos.request.OrderCreationRequest;
import africa.springCore.martbackend.portfolio.order.domain.dtos.response.OrderResponseDto;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponseDto postAnOrder(Long customerId, OrderCreationRequest orderCreationRequest) throws MapperException, ProductNotFoundException, UserNotFoundException, OrderCreationFailedException;

    BasePageableResponse<OrderResponseDto> getAllOrders(Pageable pageable, String orderStatus);

    OrderResponseDto getOrderById(Long id) throws OrderNotFoundException, MapperException, ProductNotFoundException;

    BasePageableResponse<OrderResponseDto> getCustomerOrders(Long customerId, String orderStatus, Pageable pageable) throws UserNotFoundException, MapperException;

    OrderResponseDto updateOrderStatus(Long orderId, String command) throws OrderNotFoundException, MapperException, ProductNotFoundException, OrderUpdateFailedException;

    Long retrieveTotalOrders();

}
