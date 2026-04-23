package com.ecommerce.order.service.impl;

import com.ecommerce.commondto.kafka.OrderCanceledEvent;
import com.ecommerce.commondto.kafka.OrderConfirmedEvent;
import com.ecommerce.commondto.kafka.OrderCreatedEvent;
import com.ecommerce.commondto.order.OrderCreateRequest;
import com.ecommerce.commondto.order.OrderResponse;
import com.ecommerce.commondto.order.StockItem;
import com.ecommerce.commondto.user.UserResponse;
import com.ecommerce.commonexception.exception.AccessRestrictedException;
import com.ecommerce.commonexception.exception.ResourceNotFoundException;
import com.ecommerce.kafka.topic.KafkaTopics;
import com.ecommerce.kafka.producers.KafkaEventPublisher;
import com.ecommerce.order.feign.ProductFeignClient;
import com.ecommerce.order.feign.UserFeignClient;
import com.ecommerce.order.mapper.OrderMapper;
import com.ecommerce.order.model.Order;
import com.ecommerce.order.model.OrderItem;
import com.ecommerce.order.model.OrderStatus;
import com.ecommerce.order.repository.OrderRepository;
import com.ecommerce.order.service.OrderCatalogService;
import com.ecommerce.order.service.OrderModifiedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderModifiedServiceImpl implements OrderModifiedService {

    private final OrderRepository orderRepository;
    private final UserFeignClient userClient;
    private final ProductFeignClient productClient;
    private final OrderMapper orderMapper;
    private final KafkaEventPublisher kafkaEventPublisher;
    private final OrderCatalogService orderCatalogService;

    @Override
    @Transactional
    public OrderResponse createOrder(String email, OrderCreateRequest request) {

        UserResponse user = userClient.getUserByEmail(email);

        List<OrderItem> items = request.products().stream()
                .map(item -> {
                    var productInfo = productClient.getProductById(item.productId());

                    if (productInfo.quantity() < item.quantity()) {
                        log.warn("Insufficient stock for product: {}. Available: {}", item.productId(), productInfo.quantity());
                        throw new IllegalArgumentException(String.format(
                                "Insufficient stock for '%s'. Available: %d", productInfo.name(), productInfo.quantity()));
                    }

                    return OrderItem.builder()
                            .productId(productInfo.id())
                            .productName(productInfo.name())
                            .price(productInfo.price())
                            .category(productInfo.category())
                            .quantity(item.quantity())
                            .build();
                })
                        .toList();

        BigDecimal total = items.stream()
                .map(orderItem -> orderItem.getPrice()
                        .multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);



        Order order = Order.builder()
                .userId(user.id())
                .items(items)
                .totalPrice(total)
                .status(OrderStatus.NEW)
                .orderCreateDate(LocalDateTime.now())
                .orderUpdateDate(LocalDateTime.now())
                .build();

        items.forEach(item -> item.setOrder(order));
        orderRepository.save(order);

        kafkaEventPublisher.publish(
                KafkaTopics.ORDER_CREATED,
                order.getId().toString(),
                new OrderCreatedEvent(
                order.getId(),
                user.id(),
                user.email(),
                order.getTotalPrice(),
                order.getStatus().name(),
                        orderMapper.toOrderResponse(order))
        );

        log.info("Order created and event published for orderId: {}", order.getId());

        return orderMapper.toOrderResponse(order);
    }

    @Override
    @Transactional
    public void updateOrderStatus(Long orderId, OrderStatus newStatus) {

        Order orderById = orderCatalogService.getOrderById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Order with id '%d' not found", orderId)));

            if (validateStatusTransition(orderById.getStatus(), newStatus)) {
                orderById.setStatus(newStatus);
            }

        log.info("Order status updated for orderId: {}", orderId);
    }

    @Override
    @Transactional
    @CacheEvict(value = "orders", key = "#orderId")
    public OrderResponse cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Order with id '%d' not found", orderId)));

        updateOrderStatus(orderId, OrderStatus.CANCELLED);
        orderRepository.save(order);

        var items = order.getItems()
                        .stream()
                        .map(item -> new StockItem(item.getProductId(), item.getQuantity()))
                        .toList();


        kafkaEventPublisher.publish(
                KafkaTopics.ORDER_CANCELED,
                orderId.toString(),
                new OrderCanceledEvent(
                        orderId,
                        order.getUserId(),
                        "Order has been canceled",
                        items));

        log.info("Order {} cancelled", orderId);

        return orderMapper.toOrderResponse(order);
    }


    @Override
    @Transactional
    public OrderResponse confirmOrder(Long orderId) {
        Order orderById = orderCatalogService.getOrderById(orderId)
                .orElseThrow(()-> new ResourceNotFoundException(String.format("Order with id '%d' not found", orderId)));

        log.info("Order with ID {} was found", orderId);

        updateOrderStatus(orderId, OrderStatus.CONFIRMED);

            kafkaEventPublisher.publish(
                    KafkaTopics.ORDER_CONFIRMED,
                    orderId.toString(),
                    new OrderConfirmedEvent(orderId, orderById.getUserId(), orderById.getTotalPrice()));
        log.info("Order confirmed event was published for orderId: {} ", orderId);

        return orderMapper.toOrderResponse(orderRepository.save(orderById));
    }

    @Override
    @Transactional
    @CacheEvict(value = "orders", key = "#id")
    public void deleteOrder(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        UserResponse userByEmail = userClient.getUserByEmail(email);
        if (!order.getUserId().equals(userByEmail.id())) {
            throw new AccessRestrictedException("You are not allowed to delete this order");
        }

        orderRepository.deleteById(id);
    }

    private boolean validateStatusTransition(OrderStatus current, OrderStatus next) {
        if (current == next) {
            return false;
        }

        Map<OrderStatus, Set<OrderStatus>> allowed = Map.of(
                OrderStatus.NEW,       Set.of(OrderStatus.CONFIRMED, OrderStatus.CANCELLED),
                OrderStatus.CONFIRMED, Set.of(OrderStatus.PAID,   OrderStatus.CANCELLED),
                OrderStatus.CANCELLED, Set.of()
        );

        if (!allowed.get(current).contains(next)) {
            throw new IllegalArgumentException(String.format(
                    "Cannot transition order status from %s to %s",
                    current, next));
        }

        log.info("Order status {} is allowed to transition to {}",
                current, next);
        return true;
    }
}
