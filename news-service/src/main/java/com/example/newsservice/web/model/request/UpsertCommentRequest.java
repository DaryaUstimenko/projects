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
public class UpsertCommentRequest {

    private UUID id;

    @NotNull
    @Size(min = 1, max = 255, message = "Min comment size is: {min}. Max comment size is: {max}")
    private String comment;

}
