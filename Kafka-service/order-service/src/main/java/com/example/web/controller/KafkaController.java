package com.example.web.controller;

import com.example.model.Order;
import com.example.model.OrderEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/kafka")
@RequiredArgsConstructor
public class KafkaController {

    @Value("${app.kafka.kafkaMessageTopic1}")
    private String topicName;

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody Order order) {
        OrderEvent orderEvent = new OrderEvent(order.getProduct(), order.getQuantity());
        kafkaTemplate.send(topicName, orderEvent);
        return ResponseEntity.ok("Message send to kafka to topic: " + topicName);
    }
}
