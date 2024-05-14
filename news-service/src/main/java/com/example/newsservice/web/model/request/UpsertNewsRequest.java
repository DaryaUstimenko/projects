package com.example.newsservice.web.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpsertNewsRequest {

    private UUID id;

    private String title;

    private String description;

    private String body;

}
