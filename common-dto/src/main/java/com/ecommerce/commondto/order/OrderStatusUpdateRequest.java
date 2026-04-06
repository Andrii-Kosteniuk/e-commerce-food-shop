package com.ecommerce.commondto.order;

import jakarta.validation.constraints.NotNull;

public record OrderStatusUpdateRequest(
        @NotNull(message = "Status is required")
        String status
) {}
