package com.ecommerce.commondto.order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record StockItem(
        @NotNull(message = "Product ID cannot be empty")
        Long productId,

        @NotNull(message = "Quantity cannot be null")
        @Positive(message = "Quantity must be a positive number")
        Integer quantity) {
}
