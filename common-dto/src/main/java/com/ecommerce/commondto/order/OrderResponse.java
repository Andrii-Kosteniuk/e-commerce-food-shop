package com.ecommerce.commondto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse (
        Long orderId,
        Long userId,
        List<OrderItemResponse> items,
        BigDecimal totalPrice,
        String status,
        LocalDateTime orderCreateDate,
        LocalDateTime orderUpdateDate
) {


}
