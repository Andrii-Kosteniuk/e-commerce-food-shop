package com.ecommerce.kafka.producers;

import com.ecommerce.commondto.kafka.PaymentFailedEvent;
import com.ecommerce.commondto.kafka.PaymentSucceededEvent;
import com.ecommerce.kafka.topic.KafkaTopics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishPaymentSucceeded(PaymentSucceededEvent event) {
        log.info("Publishing PaymentSucceededEvent for orderId={}, paymentId={}",
                event.orderId(), event.paymentId());
        kafkaTemplate.send(KafkaTopics.PAYMENT_SUCCEEDED, String.valueOf(event.orderId()), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish PaymentSucceededEvent for orderId={}: {}",
                                event.orderId(), ex.getMessage());
                    } else {
                        log.info("PaymentSucceededEvent published successfully for orderId={}, offset={}",
                                event.orderId(), result.getRecordMetadata().offset());
                    }
                });

    }

    public void publishPaymentFailed(PaymentFailedEvent event) {
        log.info("Publishing PaymentFailedEvent for orderId={}, paymentId={}",
                event.orderId(), event.paymentId());

        kafkaTemplate.send(KafkaTopics.PAYMENT_FAILED, String.valueOf(event.orderId()), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish PaymentFailedEvent for orderId={}: {}",
                                event.orderId(), ex.getMessage());
                    } else {
                        log.info("PaymentFailedEvent published successfully for orderId={}, offset={}",
                                event.orderId(), result.getRecordMetadata().offset());
                    }
                });
    }
}