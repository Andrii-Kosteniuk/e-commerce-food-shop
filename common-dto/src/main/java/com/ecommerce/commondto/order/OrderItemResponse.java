package com.ecommerce.commondto.order;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long productId,
        String productName,
        BigDecimal price,
        int quantity
) {}
