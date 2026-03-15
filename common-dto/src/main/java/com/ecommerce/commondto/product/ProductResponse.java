package com.ecommerce.commondto.product;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String name,
        BigDecimal price,
        int quantity,
        String category,
        String imageUrl,
        boolean available
        ) {}
