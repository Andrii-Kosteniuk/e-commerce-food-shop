package com.ecommerce.commondto.kafka;

import java.math.BigDecimal;

public record OrderCreatedEvent(Long orderId, Long userId, String userEmail, BigDecimal totalPrice, String status) {}