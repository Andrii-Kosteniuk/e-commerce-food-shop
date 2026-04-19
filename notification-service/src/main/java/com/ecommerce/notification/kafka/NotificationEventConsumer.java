package com.ecommerce.notification.kafka;

import com.ecommerce.commondto.kafka.*;
import com.ecommerce.kafka.config.KafkaTopics;
import com.ecommerce.notification.mail.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationEventConsumer {

    private final NotificationService notificationService;
    private static final String CONFIRMATION_URL = "http://localhost:9091/api/v1/orders/confirm-order/";

        @KafkaListener(topics = KafkaTopics.ORDER_CREATED, groupId = "notification-group")
        public void handleOrderCreated(OrderCreatedEvent event) {
            log.info("Sending order created notification to userId: {}", event.userId());

                notificationService.notifyOrderCreated(
                        event.userEmail(),
                        event.orderId(),
                        event.totalPrice().toString(),
                        event.response().items(), CONFIRMATION_URL + event.orderId()

                );
        }

        @KafkaListener(topics = KafkaTopics.PAYMENT_SUCCEEDED, groupId = "notification-group")
        public void handlePaymentSucceeded(PaymentSucceededEvent event) {
            log.info("Sending payment success notification to userId: {}", event.userId());

            notificationService.notifyPaymentSucceeded(
                    event.userEmail(),
                    event.orderId(),
                    event.totalPrice().toString()
            );

            log.info("Message 'PAYMENT_SUCCEEDED' has been sent to user");
        }

        @KafkaListener(topics = KafkaTopics.PAYMENT_FAILED, groupId = "notification-group")
        public void handlePaymentFailed(PaymentFailedEvent event) {
            log.info("Sending payment failed notification to userId: {}", event.userId());

            notificationService.notifyPaymentFailed(
                    event.userEmail(),
                    event.orderId(),
                    event.reason()
            );

            log.info("Message 'PAYMENT_FAILED' has been sent to user");

        }

}