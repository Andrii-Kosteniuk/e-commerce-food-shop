package com.ecommerce.commondto.order;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderCreateRequest(
        @NotNull(message = "Order list should not be 'null'!")
        @NotEmpty(message = "Order list should not be empty!")
        List<StockItem> products
) {
}
