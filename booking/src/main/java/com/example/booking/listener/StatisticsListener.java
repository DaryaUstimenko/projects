package com.example.booking.listener;

import com.example.booking.entity.BookingEvent;
import com.example.booking.entity.RegistrationEvent;
import com.example.booking.repository.BookingEventRepository;
import com.example.booking.repository.RegistrationEventRepository;
import com.example.booking.service.impl.StatisticsSavingService;
import com.example.booking.web.model.response.BookingModelEvent;
import com.example.booking.web.model.response.RegistrationModelEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@RequiredArgsConstructor
public class StatisticsListener {

    private final RegistrationEventRepository registrationEventRepository;
    private final BookingEventRepository bookingEventRepository;
    private final StatisticsSavingService statisticsSavingService;

    @KafkaListener(topics = "${app.kafka.kafkaMessageTopic1}",
            groupId = "${app.kafka.kafkaMessageGroupId}",
            containerFactory = "kafkaListenerContainerFactory")
    public void listenRegistration(@Payload RegistrationModelEvent registrationModelEvent) {

        RegistrationEvent registrationEvent = new RegistrationEvent();
        registrationEvent.setUserId(registrationModelEvent.getUserId());

        registrationEventRepository.save(registrationEvent);
        statisticsSavingService.saveToCSV();

        log.info("Received registration event and saved to database: {}", registrationModelEvent);
    }

    @KafkaListener(topics = "${app.kafka.kafkaMessageTopic2}",
            groupId = "${app.kafka.kafkaMessageGroupId}",
            containerFactory = "kafkaListenerContainerFactory")
    public void listenBooking(@Payload BookingModelEvent bookingModelEvent) {


        BookingEvent bookingEvent = new BookingEvent();
        bookingEvent.setUserId(bookingModelEvent.getUserId());
        bookingEvent.setCheckIn(bookingModelEvent.getCheckIn());
        bookingEvent.setCheckOut(bookingModelEvent.getCheckOut());

        bookingEventRepository.save(bookingEvent);
        statisticsSavingService.saveToCSV();

        log.info("Received booking event and saved to database: {}", bookingModelEvent);
    }
}

