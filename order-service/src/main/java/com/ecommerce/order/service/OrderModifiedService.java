package com.ecommerce.order.service;

import com.ecommerce.commondto.order.OrderCreateRequest;
import com.ecommerce.commondto.order.OrderResponse;
import com.ecommerce.order.model.OrderStatus;

public interface OrderModifiedService {

    OrderResponse createOrder(String email, OrderCreateRequest request);
    void updateOrderStatus(Long orderId, OrderStatus newStatus);
    OrderResponse cancelOrder(Long orderId);
    void deleteOrder(Long id);

}
