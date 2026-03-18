package com.ecommerce.commondto.order;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

public record OrderProductRequest(
        @NotEmpty(message = "Product ID cannot be empty")
        Long id,
        @Positive(message = "Quantity must be a positive number")
        int quantity) {
}
