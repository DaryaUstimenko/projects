package com.example.social_network_account.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PageAccountDto {
    private int totalPages;
    private long totalElements;
    private PageableObject pageable;
    private int size;
    private List<AccountDto> content;
    private int number;
    private SortObject sort;
    private boolean first;
    private boolean last;
    private int numberOfElements;
    private boolean empty;
}
