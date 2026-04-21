package com.ecommerce.commondto.kafka;

public record PaymentFailedEvent(
        Long paymentId,
        Long orderId,
        Long userId,
        String reason
) {}