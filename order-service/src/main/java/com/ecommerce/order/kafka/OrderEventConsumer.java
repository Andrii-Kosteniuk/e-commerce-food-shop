package com.ecommerce.order.kafka;

import com.ecommerce.commondto.kafka.OrderConfirmedEvent;
import com.ecommerce.commonexception.exception.KafkaEventException;
import com.ecommerce.kafka.topic.KafkaTopics;
import com.ecommerce.commondto.kafka.OrderCanceledEvent;
import com.ecommerce.commondto.kafka.PaymentFailedEvent;
import com.ecommerce.commondto.kafka.PaymentSucceededEvent;
import com.ecommerce.order.feign.PaymentFeignClient;
import com.ecommerce.order.model.OrderStatus;
import com.ecommerce.order.repository.OrderRepository;
import com.ecommerce.order.service.OrderModifiedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final OrderModifiedService orderModifiedService;
    private final OrderRepository orderRepository;
    private final PaymentFeignClient paymentFeignClient;


    @KafkaListener(
            topics = KafkaTopics.PAYMENT_SUCCEEDED,
            groupId = "order-group"
    )
    public void handlePaymentSucceeded(PaymentSucceededEvent event) {
        log.info("Received PaymentSucceededEvent for order: {}", event.orderId());

        if (orderRepository.existsByIdAndStatus(event.orderId(), OrderStatus.PAID)) {
            log.warn("Order {} already processed ", event.orderId());
            return;
        }

        orderModifiedService.updateOrderStatus(event.orderId(), OrderStatus.PAID);
        log.info("Order {} marked as paid", event.orderId());
    }


    @KafkaListener(
            topics = KafkaTopics.PAYMENT_FAILED,
            groupId = "order-group"
    )
    public void handlePaymentFailed(PaymentFailedEvent event) {
        log.info("Payment failed for orderId: {}, reason: {}",
                event.orderId(), event.reason());
        orderModifiedService.cancelOrder(event.orderId());
        orderModifiedService.updateOrderStatus(event.orderId(), OrderStatus.CANCELLED);

    }

    @KafkaListener(
            topics = KafkaTopics.ORDER_CONFIRMED,
            groupId = "order-group"
    )
    public void handleOrderConfirmed(OrderConfirmedEvent event) {
        try {
            orderModifiedService.updateOrderStatus(event.orderId(), OrderStatus.CONFIRMED);
            log.info("Order {} confirmed", event.orderId());

        } catch (Exception e) {
            throw new KafkaEventException("Failed to confirm order", e);
        }

    }

    @KafkaListener(
            topics = KafkaTopics.ORDER_CANCELED,
            groupId = "order-group"
    )
    public void handleOrderCanceled(OrderCanceledEvent event) {
        try {
            orderModifiedService.updateOrderStatus(event.orderId(), OrderStatus.CANCELLED);
        } catch (Exception e) {
            throw new KafkaEventException("Failed to confirm order", e);
        }

    }
}
