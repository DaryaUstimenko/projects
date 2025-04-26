package com.example.social_network_account.mapper;

import com.example.social_network_account.dto.EmailChangedEventDto;
import com.example.social_network_account.dto.RegistrationEventDto;
import com.example.social_network_account.dto.UserOnlineEventDto;
import com.example.social_network_account.exception.AccountNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventMapper {

    private final ObjectMapper objectMapper;

    public RegistrationEventDto mapRegistrationFromJson(String registrationJson) {
        try {
            return objectMapper.readValue(registrationJson, RegistrationEventDto.class);
        } catch (JsonProcessingException e) {
            throw new AccountNotFoundException("Account not found", HttpStatus.NOT_FOUND);
        }
    }

    public UserOnlineEventDto mapUserOnlineFromJson(String userOnlineJson) {
        try {
            return objectMapper.readValue(userOnlineJson, UserOnlineEventDto.class);
        } catch (JsonProcessingException e) {
            throw new AccountNotFoundException("Account not found", HttpStatus.NOT_FOUND);
        }
    }

    public EmailChangedEventDto mapEmailChangedFromJson(String emailChangedJson) {
        try {
            return objectMapper.readValue(emailChangedJson, EmailChangedEventDto.class);
        } catch (JsonProcessingException e) {
            throw new AccountNotFoundException("Account not found", HttpStatus.NOT_FOUND);
        }
    }
}