package com.ecommerce.commondto.kafka;

import com.ecommerce.commondto.order.OrderResponse;

import java.math.BigDecimal;

public record OrderCreatedEvent(Long orderId, Long userId, String userEmail, BigDecimal totalPrice, String status, OrderResponse response) {}