package com.ecommerce.commondto.kafka;

public record PaymentFailedEvent(
        Long orderId,
        Long userId,
        String userEmail,
        String reason
) {}