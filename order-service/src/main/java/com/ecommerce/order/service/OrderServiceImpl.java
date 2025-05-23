package com.ecommerce.order.service;

import com.commondto.order.OrderCreateRequest;
import com.commondto.order.OrderUpdateRequest;
import com.commonexception.exception.ResourceNotFoundException;
import com.ecommerce.order.model.Order;
import com.ecommerce.order.model.OrderStatus;
import com.ecommerce.order.repository.OrderRepository;
import com.ecommerce.user.model.User;
import com.ecommerce.user.util.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final UserServiceClient userServiceClient;
    private final UserMapper userMapper;


    @Override
    public Order createOrder(OrderCreateRequest request) {
        User user = userServiceClient.getAuthenticatedUser();

        Order order = new Order();
        order.setUser_id(user.getId());
        order.setItems(request.items());
        order.setTotalPrice(request.items().stream()
                .map(item -> item.price().multiply(new BigDecimal(item.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setDeliveryInfo(userMapper.userToUserResponse(user).deliveryInfo());

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
        faundOrder.setStatus(updateRequest.status());
        faundOrder.setDeliveryInfo(updateRequest.deliveryInfo());

        return orderRepository.save(faundOrder);
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
