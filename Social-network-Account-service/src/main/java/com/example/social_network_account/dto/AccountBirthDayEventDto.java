package com.example.social_network_account.dto;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class AccountBirthDayEventDto {

  private UUID accountId;

  private String birthDay;

  private String name;
}
