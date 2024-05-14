package com.example.newsservice.web.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsFilterRequest {

    @NotNull
    private PaginationRequest pagination;

    private String categoryName;

    private String username;

}
