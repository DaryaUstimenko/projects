package com.example.newsservice.web.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {

    private UUID id;

    private String comment;

    private String username;

    private Instant createdAt;

    private Instant updatedAt;

}
