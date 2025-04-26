package com.example.social_network_account.mapper;

import com.example.social_network_account.utils.LocalDateDeserializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class BirthDateMapper {

    private final ObjectMapper mapper;

    public LocalDate birthDayToLocalDate(String birthdayJson) {

        try {
            SimpleModule module = new SimpleModule();
            module.addDeserializer(LocalDate.class, new LocalDateDeserializer());
            mapper.registerModule(module);

            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            mapper.addMixIn(LocalDate.class, LocalDateMixin.class);

            return mapper.readValue(birthdayJson, LocalDate.class);
        } catch (JsonProcessingException e) {
            log.warn("Ошибка парсинга даты: " + e.getMessage());
            return null;
        }
    }

    private static class LocalDateMixin {
        @JsonDeserialize(using = LocalDateDeserializer.class)
        LocalDate myDate;
    }
}
