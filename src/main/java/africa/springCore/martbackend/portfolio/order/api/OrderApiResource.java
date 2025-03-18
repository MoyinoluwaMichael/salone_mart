package africa.springCore.martbackend.portfolio.order.api;

import africa.springCore.martbackend.common.data.ApiResponse;
import africa.springCore.martbackend.core.portfolio.order.exception.OrderCreationFailedException;
import africa.springCore.martbackend.core.portfolio.order.exception.OrderNotFoundException;
import africa.springCore.martbackend.core.portfolio.order.exception.OrderUpdateFailedException;
import africa.springCore.martbackend.core.portfolio.product.exception.ProductCategoryNotFoundException;
import africa.springCore.martbackend.core.portfolio.product.exception.ProductNotFoundException;
import africa.springCore.martbackend.infrastructure.exception.MapperException;
import africa.springCore.martbackend.infrastructure.exception.UserNotFoundException;
import africa.springCore.martbackend.portfolio.order.domain.dtos.request.OrderCreationRequest;
import africa.springCore.martbackend.portfolio.order.domain.dtos.request.ProductOrderCreationRequest;
import africa.springCore.martbackend.portfolio.order.domain.dtos.response.OrderListingDto;
import africa.springCore.martbackend.portfolio.order.domain.dtos.response.OrderResponseDto;
import africa.springCore.martbackend.portfolio.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import static africa.springCore.martbackend.common.Message.apiResponse;

@RequestMapping("api/v1/")
@RestController
@RequiredArgsConstructor
@Validated
public class OrderApiResource {

    private final OrderService orderService;

    @PostMapping("customers/{customerId}/orders")
    @Operation(summary = "Post a New Order")
    public ResponseEntity<OrderResponseDto> postAnOrder(
            @PathVariable(name = "customerId") Long customerId,
            @Valid @RequestBody OrderCreationRequest orderCreationRequest
    ) throws MapperException, ProductNotFoundException, UserNotFoundException, ProductCategoryNotFoundException, OrderCreationFailedException {
        OrderResponseDto orderResponseDto = orderService.postAnOrder(customerId, orderCreationRequest);
        return ResponseEntity.ok(orderResponseDto);
    }

    @PostMapping("orders/calculateTotalAmount")
    @Operation(summary = "Calculate total amount for one or more orders")
    public ResponseEntity<ApiResponse> calculateTotalAmount(
            @RequestBody List<ProductOrderCreationRequest> productOrders
    ) throws africa.springCore.martbackend.core.portfolio.product.exception.ProductCategoryNotFoundException, MapperException, africa.springCore.martbackend.core.portfolio.product.exception.ProductNotFoundException {
        BigDecimal totalAmount = orderService.calculateTotalAmount(productOrders, null);
        return ResponseEntity.ok(apiResponse(totalAmount));
    }

    @GetMapping("orders/{id}")
    @Operation(summary = "Get order by ID")
    public ResponseEntity<OrderResponseDto> getOrderById(
            @PathVariable(name = "id") Long id
    ) throws OrderNotFoundException, MapperException, ProductCategoryNotFoundException, ProductNotFoundException {
        OrderResponseDto orderResponseDto = orderService.getOrderById(id);
        return ResponseEntity.ok(orderResponseDto);
    }

    @GetMapping("orders")
    @Operation(summary = "Get all orders by status")
    public ResponseEntity<OrderListingDto> getAllOrders(
            @RequestParam(name = "orderStatus", defaultValue = "all") String orderStatus,
            @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
    )  {
        OrderListingDto orderListingDto = orderService.getAllOrders(pageable, orderStatus);
        return ResponseEntity.ok(orderListingDto);
    }


    @PatchMapping("orders/{orderId}")
    @Operation(summary = "Update order status")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(
            @PathVariable(name = "orderId") Long orderId,
            @RequestParam(name = "command", defaultValue = "checkout") String command
    ) throws OrderNotFoundException, ProductCategoryNotFoundException, OrderUpdateFailedException, MapperException, ProductNotFoundException {
        OrderResponseDto orderResponseDto = orderService.updateOrderStatus(orderId, command);
        return ResponseEntity.ok(orderResponseDto);
    }

    @GetMapping("customers/{customerId}/orders")
    @Operation(summary = "Get all Customer's orders")
    public ResponseEntity<OrderListingDto> getAllOrders(
            @PathVariable(name = "customerId") Long customerId,
            @RequestParam(name = "orderStatus", defaultValue = "all") String orderStatus,
            @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
    ) throws UserNotFoundException, MapperException {
        OrderListingDto orderListingDto = orderService.getCustomerOrders(customerId, orderStatus, pageable);
        return ResponseEntity.ok(orderListingDto);
    }

}
