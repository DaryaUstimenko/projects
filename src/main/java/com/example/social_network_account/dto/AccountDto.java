package com.example.social_network_account.dto;

import com.example.social_network_account.utils.LocalDateDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class AccountDto {

    private UUID id;
    private String email;
    private String phone;
    private String photo;
    private String profileCover;
    private String about;
    private String city;
    private String country;
    private String firstName;
    private String lastName;
    private ZonedDateTime regDate;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate birthDate;
    private String messagePermission;
    private ZonedDateTime lastOnlineTime;
    @JsonProperty("isOnline")
    private boolean isOnline;
    @JsonProperty("isBlocked")
    private boolean isBlocked;
    @JsonProperty("isDeleted")
    private boolean isDeleted;
    private String photoId;
    private String photoName;
    private ZonedDateTime createdOn;
    private ZonedDateTime updatedOn;
    private String emojiStatus;
}
