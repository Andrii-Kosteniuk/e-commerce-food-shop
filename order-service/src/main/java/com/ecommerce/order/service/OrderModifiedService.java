package com.ecommerce.order.service;

import com.ecommerce.commondto.order.OrderRequest;
import com.ecommerce.commondto.order.OrderResponse;
import com.ecommerce.order.model.Order;

import java.util.List;

public interface OrderModifiedService {

    OrderResponse createOrder(String email, OrderRequest request);
    Order updateOrder(Long orderId, OrderRequest orderRequest);
    void deleteOrder(Long id);

}
