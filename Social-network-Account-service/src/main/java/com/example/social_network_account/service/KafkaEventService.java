package com.example.social_network_account.service;

import com.example.social_network_account.dto.AccountBirthDayEventDto;
import com.example.social_network_account.dto.CreatedAccountEventDto;
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

    @Value("${app.kafka.topic.createdAccount}")
    private String createdAccountTopic;

    @Value("${app.kafka.topic.deletedAccount}")
    private String deletedAccountTopic;

    @Value("${app.kafka.topic.blockedAccount}")
    private String blockedAccountTopic;

    @Value("${app.kafka.topic.unblockedAccount}")
    private String unblockedAccountTopic;

    @Value("${app.kafka.topic.accountBirthDay}")
    private String accountBirthDay;

    private  final KafkaTemplate<String, Object> kafkaTemplate;


    public void createdAccountEvent(UUID accountId, UUID userId) {
        CreatedAccountEventDto createdAccountEventDto = new CreatedAccountEventDto(accountId, userId);
        kafkaTemplate.send(createdAccountTopic, createdAccountEventDto);
    }

    public void deletedAccountEvent(UUID accountId) {
        kafkaTemplate.send(deletedAccountTopic, accountId);
    }

    public void blockedAccountEvent(UUID accountId) {
        kafkaTemplate.send(blockedAccountTopic, accountId);
    }

    public void unBlockedAccountEvent(UUID accountId) {
        kafkaTemplate.send(unblockedAccountTopic, accountId);
    }

    public void accountBirthDayEvent(UUID accountId, String birthDay, String name) {
        AccountBirthDayEventDto birthDayEventDto = new AccountBirthDayEventDto(accountId, birthDay, name);
        kafkaTemplate.send(accountBirthDay, birthDayEventDto);
    }
}
