package com.ecommerce.order.service;

import com.commondto.order.OrderCreateRequest;
import com.commondto.order.OrderUpdateRequest;
import com.ecommerce.order.model.Order;

public interface OrderService {

    Order createOrder(OrderCreateRequest request);
    Order getOrderById(Long id);
    Order updateOrder(Long id, OrderUpdateRequest updateRequest);
    void deleteOrder(Long id);

}
