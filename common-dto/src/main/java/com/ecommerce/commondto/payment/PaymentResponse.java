package com.ecommerce.commondto.payment;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record PaymentResponse(
        Long paymentId,
        Long orderId,
        Long userId,
        BigDecimal amount,
        String currency,
        String status,
        String stripePaymentId,
        LocalDateTime createdAt
) {
}
