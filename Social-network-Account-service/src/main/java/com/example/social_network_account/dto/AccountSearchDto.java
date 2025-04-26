package com.example.social_network_account.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AccountSearchDto {

    private List<UUID> ids;
    private String author;
    private String firstName;
    private String lastName;
    private Integer ageTo;
    private Integer ageFrom;
    private String city;
    private String country;
    private Boolean isBlocked;
    private Boolean isDeleted;
}
