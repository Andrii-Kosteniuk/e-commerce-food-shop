package com.commondto.product;

import java.math.BigDecimal;


public record ItemResponse(
        Long id,
        String name,
        BigDecimal price,
        String category,
        Integer quantity,
        String imageUrl ) {}
