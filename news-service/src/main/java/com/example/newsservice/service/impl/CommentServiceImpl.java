package com.example.newsservice.service.impl;

import com.example.newsservice.entity.Comment;
import com.example.newsservice.entity.News;
import com.example.newsservice.entity.User;
import com.example.newsservice.repository.CommentRepository;
import com.example.newsservice.service.AbstractEntityService;
import com.example.newsservice.service.CommentService;
import com.example.newsservice.service.NewsService;
import com.example.newsservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class CommentServiceImpl extends AbstractEntityService<Comment, UUID, CommentRepository> implements CommentService {

    private final UserService userService;

    private final NewsService newsService;

    public CommentServiceImpl(CommentRepository repository, UserService userService, NewsService newsService) {
        super(repository);
        this.userService = userService;
        this.newsService = newsService;
    }

    @Override
    protected Comment updateFields(Comment oldEntity, Comment newEntity) {
        if (!Objects.equals(oldEntity.getComment(), newEntity.getComment())) {
            oldEntity.setComment(newEntity.getComment());
        }

        return oldEntity;
    }

    @Override
    public Page<Comment> findAllByNewsId(UUID newsId, Pageable pageable) {
        return repository.findAllByNewsId(newsId, pageable);
    }

    @Override
    public boolean existsByIdAndUserId(UUID id, UUID userId) {
        return repository.existsByIdAndUserId(id, userId);
    }

    @Override
    @Transactional
    public Comment addComment(Comment comment, UUID userId, UUID newsId) {
        User author = userService.findById(userId);
        News news = newsService.findById(newsId);

        author.addComment(comment);
        news.addComment(comment);

        return save(comment);
    }
}
