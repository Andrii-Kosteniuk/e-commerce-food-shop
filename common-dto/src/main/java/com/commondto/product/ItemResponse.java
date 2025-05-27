package com.commondto.product;

import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

@Embeddable
public record ItemResponse(
        Long id,
        String name,
        BigDecimal price,
        String category,
        Integer quantity) {}
