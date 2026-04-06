package com.ecommerce.order.controller;

import com.ecommerce.commondto.order.OrderStatusUpdateRequest;
import com.ecommerce.order.model.OrderStatus;
import com.ecommerce.order.service.OrderModifiedService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/orders")
@RequiredArgsConstructor
@Slf4j
public class AdminOrderController {

    private final OrderModifiedService modifiedService;



    @PatchMapping("/{orderId}/status")
    public ResponseEntity<Void> updateOrderStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderStatusUpdateRequest request) {

        OrderStatus newStatus = OrderStatus.valueOf(request.status().toUpperCase());
        modifiedService.updateOrderStatus(orderId, newStatus);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }



}