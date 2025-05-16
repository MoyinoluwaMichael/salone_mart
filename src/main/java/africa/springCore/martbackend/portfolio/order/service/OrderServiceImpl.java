package africa.springCore.martbackend.portfolio.order.service;

import africa.springCore.martbackend.core.domain.dtos.response.BasePageableResponse;
import africa.springCore.martbackend.core.domain.enums.OrderStatus;
import africa.springCore.martbackend.core.utils.MartMapper;
import africa.springCore.martbackend.core.portfolio.order.exception.OrderCreationFailedException;
import africa.springCore.martbackend.core.portfolio.order.exception.OrderNotFoundException;
import africa.springCore.martbackend.core.portfolio.order.exception.OrderUpdateFailedException;
import africa.springCore.martbackend.core.portfolio.product.exception.ProductNotFoundException;
import africa.springCore.martbackend.infrastructure.exception.MapperException;
import africa.springCore.martbackend.infrastructure.exception.UserNotFoundException;
import africa.springCore.martbackend.portfolio.customer.service.CustomerService;
import africa.springCore.martbackend.portfolio.order.domain.dtos.request.OrderCreationRequest;
import africa.springCore.martbackend.portfolio.order.domain.dtos.request.ProductOrderCreationRequest;
import africa.springCore.martbackend.portfolio.order.domain.dtos.response.OrderResponseDto;
import africa.springCore.martbackend.portfolio.order.domain.model.Order;
import africa.springCore.martbackend.portfolio.order.domain.repository.OrderRepository;
import africa.springCore.martbackend.portfolio.product.domain.model.Product;
import africa.springCore.martbackend.portfolio.product.domain.repository.ProductRepository;
import africa.springCore.martbackend.portfolio.product.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Locale;

import static africa.springCore.martbackend.core.utils.Message.ORDER_WITH_ID_NOT_FOUND;
import static africa.springCore.martbackend.core.utils.AppUtils.CANCEL;
import static africa.springCore.martbackend.core.utils.AppUtils.CHECKOUT;
import static africa.springCore.martbackend.core.utils.AppUtils.COMPLETE;
import static africa.springCore.martbackend.core.utils.AppUtils.DELIVER;
import static africa.springCore.martbackend.core.utils.AppUtils.IN_TRANSIT;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final MartMapper martMapper;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final CustomerService customerService;


    @Override
    @Transactional
    public OrderResponseDto postAnOrder(Long customerId, OrderCreationRequest orderCreationRequest) throws MapperException, ProductNotFoundException, UserNotFoundException, OrderCreationFailedException {
        customerService.findById(customerId);
        if (orderCreationRequest.getProductOrders().isEmpty()) {
            throw new OrderCreationFailedException("At least one product is required");
        }
        Order order = Order.instance(orderCreationRequest);
        order.setCustomerId(customerId);
        BigDecimal totalAmount = calculateTotalAmount(orderCreationRequest.getProductOrders());
        order.setTotalProductAmount(totalAmount);
        order.setOrderStatus(OrderStatus.PENDING);
        BigDecimal taxRate = BigDecimal.valueOf(0.1);
        order.setTax(order.getTotalProductAmount().multiply(taxRate));
        order.setDeliveryFee(BigDecimal.ZERO);
        order.setTotalOrderAmount();
        order = orderRepository.save(order);
        for (ProductOrderCreationRequest productOrder : orderCreationRequest.getProductOrders()) {
            Product product = productRepository.findById(productOrder.getProductId()).get();
            product.setQuantity(product.getQuantity() - productOrder.getQuantity());
            productRepository.save(product);
        }
        return martMapper.readValue(order, OrderResponseDto.class);
    }

    private OrderResponseDto getOrderResponseDto(Order order) throws MapperException, ProductNotFoundException {
        return martMapper.readValue(order, OrderResponseDto.class);
    }

    public BigDecimal calculateTotalAmount(List<ProductOrderCreationRequest> productOrders) throws MapperException, ProductNotFoundException {
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (ProductOrderCreationRequest productOrder : productOrders) {
            productService.getProductById(productOrder.getProductId());
            Product product = productRepository.findById(productOrder.getProductId()).get();
            product.setQuantity(product.getQuantity() - 1);
            productRepository.save(product);
            BigDecimal productOrderAmount = product.getPrice().multiply(BigDecimal.valueOf(productOrder.getQuantity())).setScale(3, RoundingMode.HALF_UP);
            totalAmount = totalAmount.add(productOrderAmount);
        }
        return totalAmount.setScale(0, RoundingMode.HALF_UP);
    }

    @Override
    public OrderResponseDto updateOrderStatus(Long orderId, String command) throws OrderNotFoundException, MapperException, ProductNotFoundException, OrderUpdateFailedException {
        getOrderById(orderId);
        Order order = orderRepository.findById(orderId).get();
        command = command.toLowerCase(Locale.ROOT);
        if (command.equals(CHECKOUT)) {
            if (order.getOrderStatus() != OrderStatus.PENDING)
                throw new OrderUpdateFailedException("Order with id " + orderId + " is no more in Cart");
            else order.setOrderStatus(OrderStatus.PROCESSING);
        } else if (command.equals(IN_TRANSIT)) {
            if (order.getOrderStatus() != OrderStatus.PROCESSING)
                throw new OrderUpdateFailedException("Order with id " + orderId + " is no more in checked out state");
            else order.setOrderStatus(OrderStatus.IN_TRANSIT);
        } else if (command.equals(DELIVER)) {
            if (order.getOrderStatus() != OrderStatus.IN_TRANSIT)
                throw new OrderUpdateFailedException("Order with id " + orderId + " is no more in transit");
            else order.setOrderStatus(OrderStatus.DELIVERED);
        } else if (command.equals(COMPLETE)) {
            if (order.getOrderStatus() != OrderStatus.DELIVERED)
                throw new OrderUpdateFailedException("Order with id " + orderId + " is yet to be delivered");
            else order.setOrderStatus(OrderStatus.COMPLETED);
        } else if (command.equals(CANCEL)) {
            if (order.getOrderStatus() == OrderStatus.IN_TRANSIT || order.getOrderStatus() == OrderStatus.DELIVERED || order.getOrderStatus() == OrderStatus.COMPLETED)
                throw new OrderUpdateFailedException("Order with id " + orderId + " can not be canceled. Order is already " + order.getOrderStatus());
            else order.setOrderStatus(OrderStatus.CANCELED);
        }
        return getOrderResponseDto(
                orderRepository.save(order)
        );
    }

    @Override
    public Long retrieveTotalOrders() {
        return orderRepository.count();
    }

    @Override
    public BasePageableResponse<OrderResponseDto> getAllOrders(Pageable pageable, String orderStatus) {
        if (orderStatus.equalsIgnoreCase("all")) {
            return getOrderListingDto(orderRepository.findAll(pageable));
        } else if (orderStatus.equalsIgnoreCase(OrderStatus.PENDING.name())) {
            return getOrderListingDto(orderRepository.findAllByOrderStatus(OrderStatus.PENDING, pageable));
        } else if (orderStatus.equalsIgnoreCase(OrderStatus.PROCESSING.name())) {
            return getOrderListingDto(orderRepository.findAllByOrderStatus(OrderStatus.PROCESSING, pageable));
        } else if (orderStatus.equalsIgnoreCase(OrderStatus.IN_TRANSIT.name())) {
            return getOrderListingDto(orderRepository.findAllByOrderStatus(OrderStatus.IN_TRANSIT, pageable));
        } else if (orderStatus.equalsIgnoreCase(OrderStatus.DELIVERED.name())) {
            return getOrderListingDto(orderRepository.findAllByOrderStatus(OrderStatus.DELIVERED, pageable));
        } else if (orderStatus.equalsIgnoreCase(OrderStatus.CANCELED.name())) {
            return getOrderListingDto(orderRepository.findAllByOrderStatus(OrderStatus.CANCELED, pageable));
        } else if (orderStatus.equalsIgnoreCase(OrderStatus.COMPLETED.name())) {
            return getOrderListingDto(orderRepository.findAllByOrderStatus(OrderStatus.COMPLETED, pageable));
        }
        return getOrderListingDto(orderRepository.findAll(pageable));
    }

    private BasePageableResponse<OrderResponseDto> getOrderListingDto(Page<Order> pagedOrders) {
        Page<OrderResponseDto> orderResponseDtos = pagedOrders.map((order -> {
            try {
                return getOrderResponseDto(order);
            } catch (MapperException | ProductNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }));
        return BasePageableResponse.instance(orderResponseDtos);
    }

    @Override
    public OrderResponseDto getOrderById(Long id) throws OrderNotFoundException, MapperException, ProductNotFoundException {
        return getOrderResponseDto(orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(String.format(ORDER_WITH_ID_NOT_FOUND, id))));
    }

    @Override
    public BasePageableResponse<OrderResponseDto> getCustomerOrders(Long customerId, String orderStatus, Pageable pageable) throws UserNotFoundException, MapperException {
        customerService.findById(customerId);
        if (orderStatus.equalsIgnoreCase(OrderStatus.PENDING.name())) {
            return getOrderListingDto(orderRepository.findAllByCustomerIdAndOrderStatus(customerId, OrderStatus.PENDING, pageable));
        } else if (orderStatus.equalsIgnoreCase(OrderStatus.PROCESSING.name())) {
            return getOrderListingDto(orderRepository.findAllByCustomerIdAndOrderStatus(customerId, OrderStatus.PROCESSING, pageable));
        } else if (orderStatus.equalsIgnoreCase(OrderStatus.IN_TRANSIT.name())) {
            return getOrderListingDto(orderRepository.findAllByCustomerIdAndOrderStatus(customerId, OrderStatus.IN_TRANSIT, pageable));
        } else if (orderStatus.equalsIgnoreCase(OrderStatus.DELIVERED.name())) {
            return getOrderListingDto(orderRepository.findAllByCustomerIdAndOrderStatus(customerId, OrderStatus.DELIVERED, pageable));
        } else if (orderStatus.equalsIgnoreCase(OrderStatus.CANCELED.name())) {
            return getOrderListingDto(orderRepository.findAllByCustomerIdAndOrderStatus(customerId, OrderStatus.CANCELED, pageable));
        } else if (orderStatus.equalsIgnoreCase(OrderStatus.COMPLETED.name())) {
            return getOrderListingDto(orderRepository.findAllByCustomerIdAndOrderStatus(customerId, OrderStatus.COMPLETED, pageable));
        }
        return getOrderListingDto(orderRepository.findAllByCustomerId(customerId, pageable));
    }
}
