package com.ecommerce.notification.kafka;

import com.ecommerce.kafka.topic.KafkaTopics;
import com.ecommerce.commondto.kafka.OrderCanceledEvent;
import com.ecommerce.commondto.kafka.OrderCreatedEvent;
import com.ecommerce.commondto.kafka.PaymentFailedEvent;
import com.ecommerce.commondto.kafka.PaymentSucceededEvent;
import com.ecommerce.notification.mail.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventConsumer {

    private final NotificationService notificationService;
    private static final String CONFIRMATION_URL = "http://localhost:9091/api/v1/orders/confirm-order/";

    @KafkaListener(topics = KafkaTopics.ORDER_CREATED, groupId = "notification-group")
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("Sending 'ORDER_CREATED' notification to userId: {}", event.userId());

        notificationService.notifyOrderCreated(
                event.userEmail(),
                event.orderId(),
                event.totalPrice().toString(),
                event.response().items(), CONFIRMATION_URL + event.orderId()

        );
    }


    @KafkaListener(topics = KafkaTopics.ORDER_CANCELED, groupId = "notification-group")
    public void handleOrderCanceled(OrderCanceledEvent event) {
        log.info("Sending 'ORDER_CANCELED' notification to userId: {}", event.userId());

        notificationService.notifyOrderCanceled(
                event.userId(),
                event.orderId()
        );

    }

    @KafkaListener(topics = KafkaTopics.PAYMENT_SUCCEEDED, groupId = "notification-group")
    public void handlePaymentSucceeded(PaymentSucceededEvent event) {
        log.info("Sending 'PAYMENT_SUCCEEDED' notification to userId: {}", event.userId());

        notificationService.notifyPaymentSucceeded(
                event.userId(),
                event.orderId()
        );
    }


    @KafkaListener(topics = KafkaTopics.PAYMENT_FAILED, groupId = "notification-group")
    public void handlePaymentFailed(PaymentFailedEvent event) {
        log.info("Sending 'PAYMENT_FAILED' notification to userId: {}", event.userId());

        notificationService.notifyPaymentFailed(
                event.userId(),
                event.orderId(),
                event.reason()
        );

    }

}