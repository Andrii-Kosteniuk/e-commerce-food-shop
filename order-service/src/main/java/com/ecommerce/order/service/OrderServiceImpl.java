package com.ecommerce.order.service;

import com.commondto.order.OrderCreateRequest;
import com.commondto.order.OrderUpdateRequest;
import com.commondto.user.UserResponse;
import com.commonexception.exception.ResourceNotFoundException;
import com.ecommerce.order.model.Order;
import com.ecommerce.order.model.OrderStatus;
import com.ecommerce.order.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final UserServiceClient userServiceClient;


    @Override
    public Order createOrder(OrderCreateRequest request) {
        UserResponse user = userServiceClient.getAuthenticatedUser();

        Order order = new Order();
        order.setUser_id(user.id());
        order.setItems(request.items());
        order.setTotalPrice(request.items().stream()
                .map(item -> item.price().multiply(new BigDecimal(item.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        orderRepository.save(order);

        return order;
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    @Override
    public Order updateOrder(Long id, OrderUpdateRequest updateRequest) {
        Order faundOrder = getOrderById(id);
        faundOrder.setItems(updateRequest.items());
        faundOrder.setTotalPrice(updateRequest.totalPrice());

        return orderRepository.save(faundOrder);
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
