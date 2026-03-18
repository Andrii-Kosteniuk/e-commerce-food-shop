package com.ecommerce.commondto.order;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderRequest(
        @NotNull(message = "Order list should not be 'null'!")
        List<OrderProductRequest> products
) {
}
