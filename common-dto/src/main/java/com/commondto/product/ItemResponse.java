package com.commondto.product;

import java.math.BigDecimal;
import jakarta.persistence.Embeddable;

@Embeddable
public record ItemResponse(
        Long id,
        String name,
        BigDecimal price,
        String category,
        Integer quantity,
        String imageUrl ) {}
