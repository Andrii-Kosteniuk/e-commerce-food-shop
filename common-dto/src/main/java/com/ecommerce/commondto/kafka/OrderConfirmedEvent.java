package com.ecommerce.commondto.kafka;

import java.math.BigDecimal;

public record OrderConfirmedEvent(Long orderId, Long userId, BigDecimal amount) {
}
