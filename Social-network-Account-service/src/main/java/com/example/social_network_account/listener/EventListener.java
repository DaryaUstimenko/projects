package com.example.social_network_account.listener;

import com.example.social_network_account.dto.EmailChangedEventDto;
import com.example.social_network_account.dto.RegistrationEventDto;
import com.example.social_network_account.dto.UserOnlineEventDto;
import com.example.social_network_account.mapper.EventMapper;
import com.example.social_network_account.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventListener {

    private final EventMapper eventMapper;
    private final AccountService accountService;

    @KafkaListener(topics = "${app.kafka.topic.registration}",
            groupId = "${app.kafka.kafkaMessageGroupId}",
            containerFactory = "kafkaListenerContainerFactory")
    public void listenRegistration(@Payload String registration) {
        try {
            RegistrationEventDto registrationEventDto = eventMapper.mapRegistrationFromJson(registration);
            accountService.createAccount(registrationEventDto);
        } catch (Exception e) {
            log.error("Unexpected error processing registration event: {}", registration, e);
        }
    }

    @KafkaListener(topics = "${app.kafka.topic.userIsOnline}",
            groupId = "${app.kafka.kafkaMessageGroupId}",
            containerFactory = "kafkaListenerContainerFactory")
    public void listenUserIsOnline(@Payload String userOnline) {
        try {
            UserOnlineEventDto userOnlineEventDto = eventMapper.mapUserOnlineFromJson(userOnline);
            accountService.isOnline(userOnlineEventDto.getAccountId());
        } catch (Exception e) {
            log.error("Unexpected error processing userIsOnline event: {}", userOnline, e);
        }
    }

    @KafkaListener(topics = "${app.kafka.topic.changedEmail}",
            groupId = "${app.kafka.kafkaMessageGroupId}",
            containerFactory = "kafkaListenerContainerFactory")
    public void listenEmailChangedEvent(@Payload String emailChangedEvent) {
        try {
            EmailChangedEventDto emailChangedEventDto = eventMapper.mapEmailChangedFromJson(emailChangedEvent);
            accountService.changedEmail(emailChangedEventDto.getAccountId(), emailChangedEventDto.getEmail());
        } catch (Exception e) {
            log.error("Unexpected error processing emailChanged event: {}", emailChangedEvent, e);
        }
    }
}
