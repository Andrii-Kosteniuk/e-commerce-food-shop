package com.ecommerce.payment.kafka;

import com.ecommerce.commondto.kafka.OrderConfirmedEvent;
import com.ecommerce.commondto.payment.PaymentRequest;
import com.ecommerce.commonexception.exception.KafkaEventException;
import com.ecommerce.kafka.topic.KafkaTopics;
import com.ecommerce.payment.model.Currency;
import com.ecommerce.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventConsumer {
    private final PaymentService paymentService;


    @KafkaListener(
            topics = KafkaTopics.ORDER_CONFIRMED,
            groupId = "payment-group"
    )
    public void handleOrderConfirmed(OrderConfirmedEvent event) {
        log.info("Receiving 'ORDER_CONFIRMED' event for orderId: {} ...", event.orderId());
        try {
            paymentService.createPayment(
                    new PaymentRequest(
                            event.orderId(),
                            event.userId(),
                            event.amount(),
                            Currency.USD.name(),
                            UUID.nameUUIDFromBytes((event.orderId() + ":" + event.userId()).getBytes()).toString()

                    ), event.userId()
            );

            log.info("Payment for order {} created", event.orderId());

        } catch (Exception e) {
            throw new KafkaEventException("Failed to confirm order", e);
        }
    }
}
