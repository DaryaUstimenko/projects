package com.example.newsservice.web.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsResponse {

    private UUID id;

    private String title;

    private String description;

    private String body;

    private String username;

    private Instant createdAt;

    private Instant updatedAt;

    private List<CommentResponse> comments = new ArrayList<>();

}
