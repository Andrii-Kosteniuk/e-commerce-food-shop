package com.commondto.order;

import com.commondto.product.ItemResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderUpdateRequest(
        List<ItemResponse> items,
        BigDecimal totalPrice,
        String status
) {
}
