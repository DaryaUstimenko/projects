package com.example.newsservice.web.model.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpsertCategoryRequest {

    private UUID id;

    @NotNull
    @Size(min = 3, max = 20, message = "Min size for category: {min}. Max size is: {max}")
    private String name;

}
