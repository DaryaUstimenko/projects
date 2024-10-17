package com.example.booking.service.impl;

import com.example.booking.entity.Booking;
import com.example.booking.web.model.response.BookingModelEvent;
import com.example.booking.web.model.response.RegistrationModelEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaEventService {

    @Value("${app.kafka.kafkaMessageTopic1}")
    private String topicName1;

    @Value("${app.kafka.kafkaMessageTopic2}")
    private String topicName2;

    private final KafkaTemplate<String, Object> kafkaTemplate;


    public void registrationEvent(UUID userId) {
        RegistrationModelEvent registrationEvent = new RegistrationModelEvent(userId);
        kafkaTemplate.send(topicName1, registrationEvent);

    }

    public void bookingEvent(Booking booking) {
        BookingModelEvent bookingEvent = new BookingModelEvent(
                booking.getUser().getId(), booking.getBusyFrom(), booking.getBusyTo());
        kafkaTemplate.send(topicName2, bookingEvent);
    }
}
