package com.ecommerce.notification.kafka;

import com.ecommerce.commondto.kafka.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderEventConsumer {

    @KafkaListener(topics = "order-created", groupId = "notification-group")
    public void handle(OrderCreatedEvent event) {
        log.info("Received order event — orderId: {}, userId: {}, total: {}",
                event.orderId(), event.userId(), event.totalPrice());
       log.info("Message has been sent to user");
    }
}