package com.ecommerce.commondto.kafka;

import java.math.BigDecimal;

public record PaymentCreateEvent(Long orderId, Long userId, BigDecimal amount) {
}
