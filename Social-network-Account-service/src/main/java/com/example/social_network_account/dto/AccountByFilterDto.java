package com.example.social_network_account.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccountByFilterDto {

    private AccountSearchDto accountSearchDto;
    private Integer pageNumber;
    private Integer pageSize;
}
