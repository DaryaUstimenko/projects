package com.example.newsservice.repository;

import com.example.newsservice.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {

    Page<Comment> findAllByNewsId(UUID newsId, Pageable pageable);

    boolean existsByIdAndUserId(UUID id, UUID userId);

}
