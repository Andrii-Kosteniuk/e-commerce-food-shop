package com.ecommerce.user.product.dto;

import com.ecommerce.user.product.model.Item;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ItemUpdateRequest(
        @NotBlank(message = "Name is required")
        String name,

        @NotNull(message = "Price is required")
        @Positive(message = "Price must be positive")
        BigDecimal price,

        @NotNull(message = "Category is required")
        Item.Category category,

        @NotBlank(message = "Image URL is required")
        String imageUrl
) {
}
