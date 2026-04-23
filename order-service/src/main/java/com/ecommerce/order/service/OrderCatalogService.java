package com.ecommerce.order.service;

import com.ecommerce.commondto.order.OrderResponse;
import com.ecommerce.order.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderCatalogService {

    List<OrderResponse> getAllOrders();
    Optional<Order> getOrderById(Long orderId);
}
