package com.example.social_network_account.utils;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Slf4j
public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {

    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        String dateString = p.getText();

        if (dateString == null || dateString.trim().isEmpty() || dateString.equalsIgnoreCase("none") || dateString.contains("none")) {
            log.warn("Error parsing dateString '{}'", dateString);
            return null;
        }
        try {
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateString, DateTimeFormatter.ISO_ZONED_DATE_TIME);
            return zonedDateTime.toLocalDate();
        } catch (DateTimeParseException e) {
            log.warn("Error parsing date '{}': {}", dateString, e.getMessage());
            return null;
        }
    }
}
