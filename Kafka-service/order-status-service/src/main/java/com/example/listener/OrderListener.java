package com.example.listener;

import com.example.model.OrderEvent;
import com.example.model.OrderStatusEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderListener {

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    @Value("${app.kafka.kafkaMessageTopic2}")
    private String topicName;

    @KafkaListener(topics = "${app.kafka.kafkaMessageTopic1}", groupId = "${app.kafka.kafkaMessageGroupId}",
            containerFactory = "kafkaListenerContainerFactory")
    public void listen(@Payload OrderEvent orderEvent) {
        OrderStatusEvent statusEvent = new OrderStatusEvent("CREATED ", Instant.now());
        kafkaTemplate.send(topicName, orderEvent);
        log.info("Event: {} {}", statusEvent.getStatus(), statusEvent.getDate());
    }
}
