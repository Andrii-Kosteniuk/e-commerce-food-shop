package com.ecommerce.order.service.impl;

import com.ecommerce.commondto.order.OrderItemRequest;
import com.ecommerce.commondto.order.OrderRequest;
import com.ecommerce.commondto.product.ItemResponse;
import com.ecommerce.commondto.user.UserResponse;
import com.ecommerce.commonexception.exception.ResourceNotFoundException;
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

        List<ItemResponse> orderItems = getOrderItems(request);

        log.info("Retrieved items for order: {}", orderItems);

        for (ItemResponse item : orderItems){
            orderItemService.reduceItemStock(item.id(), item.quantity());
        }

        Order order = new Order();
        order.setUserId(user.id());
        order.setItems(orderItems);
        order.setOrderCreateDate(LocalDateTime.now());
        order.setStatus(OrderStatus.NEW);
        order.setTotalPrice(calculateTotalPrice(orderItems));

        log.info("Order was created successfully: {}", order);

        return orderRepository.save(order);
    }

    private List<ItemResponse> getOrderItems(OrderRequest request) {
        return request.items().stream()
                .map(orderItemRequest -> {
                    ItemResponse item = orderItemService.getItemById(orderItemRequest.id());
                    checkIfRequestedItemsAreAvailable(orderItemRequest, item);
                    return new ItemResponse(item.id(), item.name(), item.price(), orderItemRequest.quantity(), item.category(), item.available());
                })
                .toList();
    }

    private static void checkIfRequestedItemsAreAvailable(OrderItemRequest orderItemRequest, ItemResponse item) {
        if (! item.available() || item.quantity() <= 0) {
            log.error("Item {} not available or out of stock", item.name());
            throw new ResourceNotFoundException("Item " + item.name() + " is not available or out of stock.");
        }

        if (item.quantity() - orderItemRequest.quantity() < 0) {
            throw new IllegalArgumentException("You can only give " + item.quantity() + " item of " + item.name() + " in your order, but you tried to give " + orderItemRequest.quantity());
        }
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

        List<ItemResponse> orderItems = getOrderItems(orderRequest);

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
