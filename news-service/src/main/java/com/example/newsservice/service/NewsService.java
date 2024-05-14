package com.example.newsservice.service;

import com.example.newsservice.entity.News;
import com.example.newsservice.web.model.request.NewsFilterRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface NewsService extends EntityService<News, UUID> {

    News addNews(News news, UUID userId, UUID categoryId);

    News updateNews(News news, UUID id, UUID categoryId);

    Page<News> findAllByCategoryId(UUID categoryId, Pageable pageable);

    Page<News> findAllByAuthorId(UUID userId, Pageable pageable);

    Page<News> filterBy(NewsFilterRequest filter);

    boolean existsByIdAndAuthorId(UUID id, UUID authorId);

}
