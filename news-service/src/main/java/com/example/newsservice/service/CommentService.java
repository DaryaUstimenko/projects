package com.example.newsservice.service;

import com.example.newsservice.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CommentService extends EntityService<Comment, UUID> {
    Page<Comment> findAllByNewsId(UUID newsId, Pageable pageable);

    boolean existsByIdAndUserId(UUID id, UUID userId);

    Comment addComment(Comment comment, UUID userId, UUID newsId);

}
