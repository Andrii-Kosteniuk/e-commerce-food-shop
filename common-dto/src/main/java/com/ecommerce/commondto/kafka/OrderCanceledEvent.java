package com.ecommerce.commondto.kafka;

import com.ecommerce.commondto.order.StockItem;

import java.util.List;

public record OrderCanceledEvent (Long orderId, Long userId, String reason, List<StockItem> items) {

}
