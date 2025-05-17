package africa.techimmortal.martbackend.portfolio.order.service;

import africa.techimmortal.martbackend.core.domain.dtos.response.BasePageableResponse;
import africa.techimmortal.martbackend.portfolio.order.exception.OrderCreationFailedException;
import africa.techimmortal.martbackend.portfolio.order.exception.OrderNotFoundException;
import africa.techimmortal.martbackend.portfolio.order.exception.OrderUpdateFailedException;
import africa.techimmortal.martbackend.portfolio.product.exception.ProductNotFoundException;
import africa.techimmortal.martbackend.infrastructure.exception.MapperException;
import africa.techimmortal.martbackend.infrastructure.exception.UserNotFoundException;
import africa.techimmortal.martbackend.portfolio.order.domain.dtos.request.OrderCreationRequest;
import africa.techimmortal.martbackend.portfolio.order.domain.dtos.response.OrderResponseDto;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponseDto postAnOrder(OrderCreationRequest orderCreationRequest) throws MapperException, ProductNotFoundException, UserNotFoundException, OrderCreationFailedException;

    BasePageableResponse<OrderResponseDto> getAllOrders(Pageable pageable, String orderStatus);

    OrderResponseDto getOrderById(Long id) throws OrderNotFoundException, MapperException, ProductNotFoundException;

    BasePageableResponse<OrderResponseDto> getCustomerOrders(Long customerId, String orderStatus, Pageable pageable) throws UserNotFoundException, MapperException;

    OrderResponseDto updateOrderStatus(Long orderId, String command) throws OrderNotFoundException, MapperException, ProductNotFoundException, OrderUpdateFailedException;

    Long retrieveTotalOrders();

    BasePageableResponse<OrderResponseDto> retrieveAllVendorOrders(Long vendorId, String orderStatus, Pageable pageable) throws UserNotFoundException, MapperException;
}
