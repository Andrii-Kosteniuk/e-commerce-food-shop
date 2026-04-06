package com.ecommerce.commondto.kafka;

import com.ecommerce.commondto.order.StockItem;

import java.util.List;

public record StockReleasedEvent(
        Long orderId,
        List<StockItem> items
) {}