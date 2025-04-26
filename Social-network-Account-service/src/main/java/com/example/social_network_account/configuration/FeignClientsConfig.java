package com.example.social_network_account.configuration;

import com.example.social_network_account.client.AuthServiceClient;
import com.example.social_network_account.client.FriendServiceClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import feign.Logger;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FeignClientsConfig {

    private final ObjectMapper objectMapper;

    @Value("${gateway.api.url}")
    private String gatewayApiUrl;

    @Bean
    public AuthServiceClient authServiceClient() {
        return Feign.builder()
                .encoder(new JacksonEncoder(objectMapper))
                .decoder(new JacksonDecoder(objectMapper))
                .logger(new feign.slf4j.Slf4jLogger(AuthServiceClient.class))
                .logLevel(Logger.Level.FULL)
                .target(AuthServiceClient.class, gatewayApiUrl + "/api/v1/auth");
    }

    @Bean
    public FriendServiceClient friendServiceClient() {
        return Feign.builder()
                .encoder(new JacksonEncoder(objectMapper))
                .decoder(new JacksonDecoder(objectMapper))
                .logger(new feign.slf4j.Slf4jLogger(AuthServiceClient.class))
                .logLevel(Logger.Level.FULL)
                .target(FriendServiceClient.class, gatewayApiUrl + "/api/v1/friends");
    }
}
