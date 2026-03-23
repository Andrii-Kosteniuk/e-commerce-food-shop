package com.ecommerce.order.controller;

import com.ecommerce.commondto.order.OrderResponse;
import com.ecommerce.commondto.order.OrderRequest;
import com.ecommerce.order.mapper.OrderMapper;
import com.ecommerce.order.model.Order;
import com.ecommerce.order.service.OrderCatalogService;
import com.ecommerce.order.service.OrderModifiedService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderCatalogService catalogService;
    private final OrderModifiedService modifiedService;
    private final OrderMapper orderMapper;

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {

        List<OrderResponse> allOrders = catalogService.getAllOrders();
        return ResponseEntity.ok(allOrders);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<OrderResponse> createOrder(
            @PathVariable  Long userId,
            @Valid @RequestBody OrderRequest request) {

        var order = modifiedService.createOrder(userId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @PutMapping("/update-order/{orderId}")
    public ResponseEntity<OrderResponse> updateOrder(@PathVariable("orderId") Long orderId, @RequestBody OrderRequest request) {

        Order order = modifiedService.updateOrder(orderId, request);

        return ResponseEntity.ok(orderMapper.toOrderResponse(order));
    }

    @DeleteMapping("/delete-order/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        modifiedService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
