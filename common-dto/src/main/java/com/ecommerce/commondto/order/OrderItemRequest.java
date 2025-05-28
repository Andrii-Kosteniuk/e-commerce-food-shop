package com.ecommerce.commondto.order;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

public record OrderItemRequest(
        @NotEmpty(message = "Item IDs cannot be empty")
        Long id,
        @Positive(message = "Quantity must be a positive number")
        int quantity) {
}
