package com.ecommerce.product.kafka;

import com.ecommerce.commondto.order.OrderItemResponse;
import com.ecommerce.commonexception.exception.InsufficientStockException;
import com.ecommerce.commonexception.exception.KafkaEventException;
import com.ecommerce.kafka.topic.KafkaTopics;
import com.ecommerce.commondto.kafka.OrderCanceledEvent;
import com.ecommerce.commondto.kafka.OrderCreatedEvent;
import com.ecommerce.product.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductEventConsumer {

    private final InventoryService inventoryService;

    @KafkaListener(topics = KafkaTopics.ORDER_CREATED, groupId = "product-group")
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        log.info("Reserving order created event for orderId: {}", event.orderId());

        try {
            List<OrderItemResponse> items = event.response().items();
            items.forEach(item -> {
                inventoryService.decreaseStock(item.productId(), item.quantity());
                log.info("Successfully decreased stock for product ID: '{}'", item.productId());
            });

        } catch (InsufficientStockException e) {
            log.warn("Stock insufficient for orderId: {} — {}", event.orderId(), e.getMessage());
            throw new KafkaEventException("Failed to reserve stock for order ID: '" + event.orderId() + "'", e.getCause());
        }

    }


    @KafkaListener(topics = KafkaTopics.ORDER_CANCELED, groupId = "product-group")
    public void handleOrderCanceled(OrderCanceledEvent event) {
        log.info("Receiving " + KafkaTopics.ORDER_CANCELED + " event for orderId: {}", event.orderId());
        try {
            event.items().forEach(item -> {
                inventoryService.increaseStock(item.productId(), item.quantity());
                log.info("Successfully released stock for product: {}", item.productId());
            });
        } catch (IllegalArgumentException | ResourceNotFoundException e) {
            log.error("Failed to release stock for order ID: '{}'", event.orderId(), e);
        }

    }

}