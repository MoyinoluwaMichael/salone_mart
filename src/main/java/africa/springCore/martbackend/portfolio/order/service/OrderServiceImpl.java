package africa.springCore.martbackend.portfolio.order.service;

import africa.springCore.martbackend.common.enums.OrderStatus;
import africa.springCore.martbackend.common.utils.MartMapper;
import africa.springCore.martbackend.core.portfolio.order.exception.OrderCreationFailedException;
import africa.springCore.martbackend.core.portfolio.order.exception.OrderNotFoundException;
import africa.springCore.martbackend.core.portfolio.order.exception.OrderUpdateFailedException;
import africa.springCore.martbackend.core.portfolio.product.exception.ProductCategoryNotFoundException;
import africa.springCore.martbackend.core.portfolio.product.exception.ProductNotFoundException;
import africa.springCore.martbackend.infrastructure.configuration.ApplicationProperty;
import africa.springCore.martbackend.infrastructure.exception.MapperException;
import africa.springCore.martbackend.infrastructure.exception.UserNotFoundException;
import africa.springCore.martbackend.portfolio.customer.service.CustomerService;
import africa.springCore.martbackend.portfolio.order.domain.dtos.request.OrderCreationRequest;
import africa.springCore.martbackend.portfolio.order.domain.dtos.request.ProductOrderCreationRequest;
import africa.springCore.martbackend.portfolio.order.domain.dtos.response.OrderListingDto;
import africa.springCore.martbackend.portfolio.order.domain.dtos.response.OrderResponseDto;
import africa.springCore.martbackend.portfolio.order.domain.dtos.response.ProductOrderResponseDto;
import africa.springCore.martbackend.portfolio.order.domain.model.Order;
import africa.springCore.martbackend.portfolio.order.domain.repository.OrderRepository;
import africa.springCore.martbackend.portfolio.product.domain.model.Product;
import africa.springCore.martbackend.portfolio.product.domain.repository.ProductRepository;
import africa.springCore.martbackend.portfolio.product.service.ProductService;
import africa.springCore.martbackend.portfolio.vendor.service.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static africa.springCore.martbackend.common.Message.ORDER_WITH_ID_NOT_FOUND;
import static africa.springCore.martbackend.common.utils.AppUtils.CANCEL;
import static africa.springCore.martbackend.common.utils.AppUtils.CHECKOUT;
import static africa.springCore.martbackend.common.utils.AppUtils.COMPLETE;
import static africa.springCore.martbackend.common.utils.AppUtils.DELIVER;
import static africa.springCore.martbackend.common.utils.AppUtils.IN_TRANSIT;
import static africa.springCore.martbackend.common.utils.AppUtils.NEW_ORDER;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final MartMapper martMapper;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final CustomerService customerService;
    private final VendorService vendorService;
    private final ApplicationProperty applicationProperty;


    @Override
    public OrderResponseDto postAnOrder(Long customerId, OrderCreationRequest orderCreationRequest) throws MapperException, ProductNotFoundException, UserNotFoundException, ProductCategoryNotFoundException, OrderCreationFailedException {
        customerService.findById(customerId);
        if (orderCreationRequest.getProductOrders().size() < 1) {
            throw new OrderCreationFailedException("At least one order is required");
        }
        Order order = martMapper.readValue(orderCreationRequest, Order.class);
        order.setCustomerId(customerId);
        BigDecimal totalAmount = calculateTotalAmount(orderCreationRequest.getProductOrders(), NEW_ORDER);
        order.setTotalAmount(totalAmount);
        order.setOrderStatus(OrderStatus.IN_CART);
        order.setTotalOrderAmount(totalAmount.add(orderCreationRequest.getDeliveryFee()));
        return getOrderResponseDto(orderRepository.save(order));
    }

    private OrderResponseDto getOrderResponseDto(Order order) throws MapperException, ProductCategoryNotFoundException, ProductNotFoundException {
        OrderResponseDto orderResponseDto = martMapper.readValue(order, OrderResponseDto.class);
        for (int i = 0; i < order.getProductOrders().size(); i++) {
            ProductOrderResponseDto productOrderResponseDto = orderResponseDto.getProductOrders().get(i);
            Long productId = order.getProductOrders().get(i).getProductId();
            Long quantity = order.getProductOrders().get(i).getQuantity();
            productOrderResponseDto.setProduct(productService.getProductById(productId));
            ProductOrderCreationRequest request = new ProductOrderCreationRequest();
            request.setProductId(productId);
            request.setQuantity(quantity);
            productOrderResponseDto.setPrice(calculateTotalAmount(Collections.singletonList(request), null));
        }
        return orderResponseDto;
    }

    @Override
    public BigDecimal calculateTotalAmount(List<ProductOrderCreationRequest> productOrders, String orderType) throws MapperException, ProductNotFoundException, ProductCategoryNotFoundException {
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (ProductOrderCreationRequest productOrder : productOrders) {
            productService.getProductById(productOrder.getProductId());
            Product product = productRepository.findById(productOrder.getProductId()).get();
            if (orderType != null && orderType.equals(NEW_ORDER)) {
                product.setQuantity(product.getQuantity() - 1);
                productRepository.save(product);
            }
            BigDecimal productOrderAmount = product.getPrice().multiply(BigDecimal.valueOf(productOrder.getQuantity())).setScale(3, RoundingMode.HALF_UP);
            totalAmount = totalAmount.add(productOrderAmount);
        }
        return totalAmount.setScale(0, RoundingMode.HALF_UP);
    }

    @Override
    public OrderResponseDto updateOrderStatus(Long orderId, String command) throws OrderNotFoundException, ProductCategoryNotFoundException, MapperException, ProductNotFoundException, OrderUpdateFailedException {
        getOrderById(orderId);
        Order order = orderRepository.findById(orderId).get();
        command = command.toLowerCase(Locale.ROOT);
        if (command.equals(CHECKOUT)) {
            if (order.getOrderStatus() != OrderStatus.IN_CART)
                throw new OrderUpdateFailedException("Order with id " + orderId + " is no more in Cart");
            else order.setOrderStatus(OrderStatus.CHECKED_OUT);
        } else if (command.equals(IN_TRANSIT)) {
            if (order.getOrderStatus() != OrderStatus.CHECKED_OUT)
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
    public OrderListingDto getAllOrders(Pageable pageable, String orderStatus) {
        if (orderStatus.equalsIgnoreCase("all")) {
            return getOrderListingDto(orderRepository.findAll(pageable));
        } else if (orderStatus.equalsIgnoreCase(OrderStatus.IN_CART.name())) {
            return getOrderListingDto(orderRepository.findAllByOrderStatus(OrderStatus.IN_CART, pageable));
        } else if (orderStatus.equalsIgnoreCase(OrderStatus.CHECKED_OUT.name())) {
            return getOrderListingDto(orderRepository.findAllByOrderStatus(OrderStatus.CHECKED_OUT, pageable));
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

    private OrderListingDto getOrderListingDto(Page<Order> pagedOrders) {
        Page<OrderResponseDto> orderResponseDtos = pagedOrders.map((order -> {
            try {
                return getOrderResponseDto(order);
            } catch (MapperException | ProductNotFoundException | ProductCategoryNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }));
        OrderListingDto orderListingDto = new OrderListingDto();
        orderListingDto.setOrders(orderResponseDtos.getContent());
        orderListingDto.setPageNumber(orderResponseDtos.getNumber());
        orderListingDto.setPageSize(orderResponseDtos.getSize());
        return orderListingDto;
    }

    @Override
    public OrderResponseDto getOrderById(Long id) throws OrderNotFoundException, MapperException, ProductCategoryNotFoundException, ProductNotFoundException {
        return getOrderResponseDto(orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(String.format(ORDER_WITH_ID_NOT_FOUND, id))));
    }

    @Override
    public OrderListingDto getCustomerOrders(Long customerId, String orderStatus, Pageable pageable) throws UserNotFoundException, MapperException {
        customerService.findById(customerId);
        if (orderStatus.equalsIgnoreCase(OrderStatus.IN_CART.name())) {
            return getOrderListingDto(orderRepository.findAllByCustomerIdAndOrderStatus(customerId, OrderStatus.IN_CART, pageable));
        } else if (orderStatus.equalsIgnoreCase(OrderStatus.CHECKED_OUT.name())) {
            return getOrderListingDto(orderRepository.findAllByCustomerIdAndOrderStatus(customerId, OrderStatus.CHECKED_OUT, pageable));
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
