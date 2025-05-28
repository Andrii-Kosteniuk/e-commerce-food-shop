package com.ecommerce.order.controller;

import com.ecommerce.commondto.order.OrderResponse;
import com.ecommerce.commondto.order.OrderRequest;
import com.ecommerce.order.mapper.OrderMapper;
import com.ecommerce.order.model.Order;
import com.ecommerce.order.service.OrderItemService;
import com.ecommerce.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @PostMapping("/new-order")
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest request) {
        log.info("Received request to create order: {}", request);

        var order = orderService.createOrder(request);

        log.info("Order created successfully: {}", order);

        return ResponseEntity.status(HttpStatus.CREATED).body(orderMapper.toOrderResponse(order));
    }

    @PutMapping("/update-order/{orderId}")
    public ResponseEntity<OrderResponse> updateOrder(@PathVariable("orderId") Long orderId, @RequestBody OrderRequest request) {

        Order order = orderService.updateOrder(orderId, request);

        return ResponseEntity.ok(orderMapper.toOrderResponse(order));
    }

    @DeleteMapping("/delete-order/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }



}
