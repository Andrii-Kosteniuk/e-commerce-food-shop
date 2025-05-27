package com.ecommerce.order.service.impl;

import com.commondto.order.OrderRequest;
import com.commondto.product.ItemResponse;
import com.commondto.user.UserResponse;
import com.commonexception.exception.ResourceNotFoundException;
import com.ecommerce.order.feign.FeignAuthClient;
import com.ecommerce.order.model.Order;
import com.ecommerce.order.model.OrderStatus;
import com.ecommerce.order.repository.OrderRepository;
import com.ecommerce.order.service.OrderItemService;
import com.ecommerce.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final FeignAuthClient feignAuthClient;
    private final OrderItemService orderItemService;

    @Override
    public Order createOrder(OrderRequest request) {
        log.info("Retrieving authenticated user ...");

        UserResponse user = feignAuthClient.getAuthenticatedUser();
        Order order = new Order();

        List<ItemResponse> orderItems = mapToOrderItems(request);

        order.setUserId(user.id());
        order.setItems(orderItems);
        order.setOrderCreateDate(LocalDateTime.now());
        order.setStatus(OrderStatus.NEW);
        order.setTotalPrice(calculateTotalPrice(orderItems));
        log.info("Creating order with details: {}", order);

        return orderRepository.save(order);
    }

    private List<ItemResponse> mapToOrderItems(OrderRequest request) {
        return request.ids().stream()
                .map(orderItemService::getItemById)
                .toList();
    }

    public BigDecimal calculateTotalPrice(List<ItemResponse> items) {
        return items.stream()
                .map(item -> item.price().multiply(new BigDecimal(item.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    @Override
    public Order updateOrder(Long orderId, OrderRequest orderRequest) {
        log.info("Updating order with ID: {}", orderId);

        Order faundOrder = getOrderById(orderId);
        log.info("Order found: {}", faundOrder);

        List<ItemResponse> orderItems = mapToOrderItems(orderRequest);

        faundOrder.setItems(orderItems);
        faundOrder.setTotalPrice(calculateTotalPrice(orderItems));
        faundOrder.setStatus(OrderStatus.CONFIRMED);
        faundOrder.setOrderUpdateDate(LocalDateTime.now());

        return orderRepository.save(faundOrder);
    }

    @Override
    public void deleteOrder(Long id) {
        log.info("Deleting order with ID: {}", id);
        orderRepository.deleteById(id);
    }
}
