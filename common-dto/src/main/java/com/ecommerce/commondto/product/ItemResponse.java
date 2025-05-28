package com.ecommerce.commondto.product;

import jakarta.persistence.Embeddable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Embeddable
public record ItemResponse(
        Long id,
        String name,
        BigDecimal price,
        int quantity,
        String category,
        boolean available
        ) {}
