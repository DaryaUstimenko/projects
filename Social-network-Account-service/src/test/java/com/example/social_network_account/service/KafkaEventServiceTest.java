package com.example.social_network_account.service;

import com.example.social_network_account.service.KafkaEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class KafkaEventServiceTest {

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private KafkaEventService kafkaEventService;

    private UUID accountId;
    private UUID userId;
    private String birthDay;
    private String name;


    @BeforeEach
    void setUp() {
        accountId = UUID.randomUUID();
        userId = UUID.randomUUID();
        birthDay = "1985-03-15";
        name = "Test User";
    }

    @Test
    void createdAccountEvent_shouldSendToKafka() {
        kafkaEventService.createdAccountEvent(accountId, userId);

        verify(kafkaTemplate, times(1)).send(any(), any());
    }

    @Test
    void deletedAccountEvent_shouldSendToKafka() {
        kafkaEventService.deletedAccountEvent(accountId);

        verify(kafkaTemplate, times(1)).send(any(), eq(accountId));
    }

    @Test
    void blockedAccountEvent_shouldSendToKafka() {
        kafkaEventService.blockedAccountEvent(accountId);

        verify(kafkaTemplate, times(1)).send(any(), eq(accountId));
    }

    @Test
    void unBlockedAccountEvent_shouldSendToKafka() {
        kafkaEventService.unBlockedAccountEvent(accountId);

        verify(kafkaTemplate, times(1)).send(any(), eq(accountId));
    }

    @Test
    void accountBirthDayEvent_shouldSendToKafka() {
        kafkaEventService.accountBirthDayEvent(accountId, birthDay, name);

        verify(kafkaTemplate, times(1)).send(any(), any());

    }
}