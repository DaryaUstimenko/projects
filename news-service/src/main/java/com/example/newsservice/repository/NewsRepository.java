package com.example.newsservice.repository;

import com.example.newsservice.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface NewsRepository extends JpaRepository<News, UUID>, JpaSpecificationExecutor<News> {

    Page<News> findAllByCategoryId(UUID categoryId, Pageable pageable);

    Page<News> findAllByAuthorId(UUID userId, Pageable pageable);

    boolean existsByIdAndAuthorId(UUID id, UUID authorId);

}
