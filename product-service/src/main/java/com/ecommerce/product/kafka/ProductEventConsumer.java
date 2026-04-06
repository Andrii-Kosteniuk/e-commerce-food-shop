package com.ecommerce.product.kafka;

import com.ecommerce.commondto.kafka.PaymentSucceededEvent;
import com.ecommerce.commondto.kafka.StockReleasedEvent;
import com.ecommerce.commondto.kafka.StockReservedEvent;
import com.ecommerce.commondto.order.StockItem;
import com.ecommerce.kafka.config.KafkaTopics;
import com.ecommerce.kafka.producers.KafkaEventPublisher;
import com.ecommerce.product.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductEventConsumer {

    private final InventoryService inventoryService;
    private final KafkaEventPublisher kafkaEventPublisher;

    @KafkaListener(
            topics = KafkaTopics.PAYMENT_SUCCEEDED,
            groupId = "product-group"
    )
    public void handlePaymentSucceeded(PaymentSucceededEvent event) {
        log.info("Reserving stock for orderId: {}", event.orderId());

        List<StockItem> reservedItems = new ArrayList<>();

        event.items().forEach(item -> {
            inventoryService.decreaseStock(item.productId(), item.quantity());
            reservedItems.add(new StockItem(item.productId(), item.quantity()));
        });

        kafkaEventPublisher.publish(
                KafkaTopics.STOCK_RESERVED,
                new StockReservedEvent(event.orderId(), reservedItems)
        );
    }

    @KafkaListener(
            topics = KafkaTopics.STOCK_RELEASED,
            groupId = "product-group"
    )
    public void handleStockReleased(StockReleasedEvent event) {
        log.info("Releasing stock for orderId: {}", event.orderId());

        try {
            event.items().forEach(item -> {
                inventoryService.increaseStock(item.productId(), item.quantity());
                log.info("Successfully released stock for product: {}", item.productId());
            });

        }catch (Exception e){
            log.error("Failed to release stock for order ID: '{}'", event.orderId(), e);
            throw e;
        }
    }
}