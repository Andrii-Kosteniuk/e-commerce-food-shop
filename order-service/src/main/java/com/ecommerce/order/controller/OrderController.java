package com.ecommerce.order.controller;

import com.ecommerce.commondto.order.OrderResponse;
import com.ecommerce.commondto.order.OrderCreateRequest;
import com.ecommerce.order.service.OrderCatalogService;
import com.ecommerce.order.service.OrderModifiedService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderModifiedService modifiedService;
    private final OrderCatalogService catalogService;

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(catalogService.getAllOrders());
    }

    @PostMapping("/confirm-order/{orderId}")
    public ResponseEntity<OrderResponse> confirmOrder(@PathVariable Long orderId) {

        return ResponseEntity.ok(modifiedService.confirmOrder(orderId));
    }

    @PostMapping("/create")
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderCreateRequest request) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        var order = modifiedService.createOrder(email, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable Long orderId) {

        return ResponseEntity.ok(modifiedService.cancelOrder(orderId));
    }

    @DeleteMapping("/delete-order/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        modifiedService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
