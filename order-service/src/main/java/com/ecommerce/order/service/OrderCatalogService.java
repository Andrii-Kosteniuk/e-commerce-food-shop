package com.ecommerce.order.service;

import com.ecommerce.commondto.order.OrderResponse;

import java.util.List;

public interface OrderCatalogService {

    List<OrderResponse> getAllOrders();
}
