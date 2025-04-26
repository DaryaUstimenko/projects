package com.example.social_network_account.dto;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class RegistrationEventDto {

    private UUID userId;

    private String email;

    private String firstName;

    private String lastName;
}