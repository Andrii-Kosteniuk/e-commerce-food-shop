package com.ecommerce.kafka.producers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publish(String topic, String id,  Object event) {

        log.info("Publishing {} event for ID {}", event.getClass().getSimpleName(), id);

        kafkaTemplate.send(topic, id, event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish {} for ID={} reason: {}" , event.getClass().getSimpleName(), id, ex.getMessage());
                    } else {
                        log.info("Event published successfully for ID={}, offset={}",
                                id,  result.getRecordMetadata().offset());
                    }
                });
    }

}