package com.commondto.product;

import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

@Embeddable
public record OrderedItemDTO(
        String name,
        BigDecimal price,
        Integer quantity,
        String imageUrl,
        String category) {
}
