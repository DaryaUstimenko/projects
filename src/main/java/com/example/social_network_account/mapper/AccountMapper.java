package com.example.social_network_account.mapper;

import com.example.social_network_account.entity.Account;

import com.example.social_network_account.dto.AccountDto;

import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public Account accountDtoToAccountEntity(AccountDto accountDto) {
        return Account.builder()
                .id(accountDto.getId())
                .email(accountDto.getEmail())
                .phone(accountDto.getPhone())
                .photo(accountDto.getPhoto())
                .profileCover(accountDto.getProfileCover())
                .about(accountDto.getAbout())
                .city(accountDto.getCity())
                .country(accountDto.getCountry())
                .firstName(accountDto.getFirstName())
                .lastName(accountDto.getLastName())
                .birthDate(accountDto.getBirthDate())
                .messagePermission(accountDto.getMessagePermission())
                .lastOnlineTime(accountDto.getLastOnlineTime())
                .isOnline(accountDto.isOnline())
                .isBlocked(accountDto.isBlocked())
                .isDeleted(accountDto.isDeleted())
                .photoId(accountDto.getPhotoId())
                .photoName(accountDto.getPhotoName())
                .createdOn(accountDto.getCreatedOn())
                .updatedOn(accountDto.getUpdatedOn())
                .emojiStatus(accountDto.getEmojiStatus())
                .build();
    }

    public AccountDto accountEntityToAccountDto(Account account) {
        return AccountDto.builder()
                .id(account.getId())
                .email(account.getEmail())
                .phone(account.getPhone())
                .profileCover(account.getProfileCover())
                .photo(account.getPhoto())
                .about(account.getAbout())
                .city(account.getCity())
                .country(account.getCountry())
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .birthDate(account.getBirthDate())
                .messagePermission(account.getMessagePermission())
                .lastOnlineTime(account.getLastOnlineTime())
                .isOnline(account.isOnline())
                .isBlocked(account.isBlocked())
                .isDeleted(account.isDeleted())
                .photoId(account.getPhotoId())
                .photoName(account.getPhotoName())
                .createdOn(account.getCreatedOn())
                .updatedOn(account.getUpdatedOn())
                .emojiStatus(account.getEmojiStatus())
                .build();
    }
}
