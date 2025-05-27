package com.ecommerce.order.service;

import com.commondto.order.OrderRequest;
import com.ecommerce.order.model.Order;


public interface OrderService {

    Order createOrder(OrderRequest request);
    Order getOrderById(Long id);
    Order updateOrder(Long orderId, OrderRequest orderRequest);
    void deleteOrder(Long id);

}
