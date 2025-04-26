package com.example.social_network_account.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter
public class PageableObject {
    private boolean paged;
    private int pageNumber;
    private int pageSize;
    private int offset;
    private SortObject sortObject;
    private boolean unpaged;
}
