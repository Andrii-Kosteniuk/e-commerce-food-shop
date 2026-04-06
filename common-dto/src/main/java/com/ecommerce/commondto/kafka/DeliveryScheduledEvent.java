package com.ecommerce.commondto.kafka;

import java.time.LocalDateTime;

public record DeliveryScheduledEvent(
        Long orderId,
        Long userId,
        String userEmail,
        LocalDateTime estimatedDelivery
) {}