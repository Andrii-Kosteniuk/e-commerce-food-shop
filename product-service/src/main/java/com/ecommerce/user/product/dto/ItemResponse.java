package com.ecommerce.user.product.dto;

import java.math.BigDecimal;


public record ItemResponse(
        Long id,
        String name,
        BigDecimal price,
        String category,
        Integer quantityInStock,
        String imageUrl ) {}
