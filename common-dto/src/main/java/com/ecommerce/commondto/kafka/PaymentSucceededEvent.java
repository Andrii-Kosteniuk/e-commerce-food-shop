package com.ecommerce.commondto.kafka;

import com.ecommerce.commondto.order.StockItem;

import java.math.BigDecimal;
import java.util.List;

public record PaymentSucceededEvent(
        Long orderId,
        Long userId,
        String userEmail,
        BigDecimal totalPrice,
        String transactionId,
        List<StockItem> items
) {}