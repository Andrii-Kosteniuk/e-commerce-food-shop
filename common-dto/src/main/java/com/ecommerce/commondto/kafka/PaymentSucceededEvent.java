package com.ecommerce.commondto.kafka;

public record PaymentSucceededEvent(
        Long paymentId,
        Long orderId,
        Long userId,
        String stripePaymentId
) {}