package com.ecommerce.product.dto;

import com.ecommerce.product.model.Item;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ItemCreateRequest(
        @NotBlank(message = "Name is required")
        String name,

        @NotNull(message = "Price is required")
        @Positive(message = "Price must be positive")
        BigDecimal price,

        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be positive")
        Integer quantity,

        @NotNull(message = "Category is required")
        String category,

        @NotBlank(message = "Image URL is required")
        String imageUrl,

        @NotNull(message = "Define if this item is available")
        boolean isAvailable
) {
}
