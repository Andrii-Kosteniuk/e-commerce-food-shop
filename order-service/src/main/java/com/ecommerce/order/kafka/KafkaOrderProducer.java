//package com.ecommerce.order.kafka;
//
//import com.ecommerce.commondto.kafka.OrderCreatedEvent;
//import lombok.RequiredArgsConstructor;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class KafkaOrderProducer {
//    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;
//
//    private static final String TOPIC = "order-created";
//
//    public void publish(OrderCreatedEvent event) {
//        kafkaTemplate.send(TOPIC, String.valueOf(event.orderId()), event);
//    }
//
//}
//b