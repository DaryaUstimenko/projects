package com.example.newsservice.web.controller;

import com.example.newsservice.entity.News;
import com.example.newsservice.mapper.NewsMapper;
import com.example.newsservice.service.NewsService;
import com.example.newsservice.web.model.request.NewsFilterRequest;
import com.example.newsservice.web.model.request.UpsertNewsRequest;
import com.example.newsservice.web.model.response.BriefNewsResponse;
import com.example.newsservice.web.model.response.ModelListResponse;
import com.example.newsservice.web.model.response.NewsResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    private final NewsMapper newsMapper;

    @GetMapping
    public ResponseEntity<ModelListResponse<BriefNewsResponse>> filterBy(@Valid NewsFilterRequest request) {
        Page<News> news= newsService.filterBy(request);

        return ResponseEntity.ok(
                ModelListResponse.<BriefNewsResponse>builder()
                        .totalCount(news.getTotalElements())
                        .data(news.stream().map(newsMapper::newsToBriefResponse).toList())
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(newsMapper.newsToResponse(newsService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<NewsResponse> createNews(@RequestBody UpsertNewsRequest request,
                                                   @RequestParam UUID userId,
                                                   @RequestParam UUID categoryId) {
        News newPost = newsService.addNews(newsMapper.upsertRequestToNews(request), userId, categoryId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(newsMapper.newsToResponse(newPost));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NewsResponse> updateNews(@RequestBody UpsertNewsRequest request,
                                                   @PathVariable UUID id,
                                                   @RequestParam(required = false) UUID categoryId) {
         News updatedPost = newsService.updateNews(newsMapper.upsertRequestToNews(request), id, categoryId);

        return ResponseEntity.ok(newsMapper.newsToResponse(updatedPost));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNewsById(@PathVariable UUID id) {
        newsService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

}
