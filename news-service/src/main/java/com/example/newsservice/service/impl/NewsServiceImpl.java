package com.example.newsservice.service.impl;

import com.example.newsservice.entity.Category;
import com.example.newsservice.entity.News;
import com.example.newsservice.entity.User;
import com.example.newsservice.repository.NewsSpecification;
import com.example.newsservice.repository.NewsRepository;
import com.example.newsservice.service.AbstractEntityService;
import com.example.newsservice.service.CategoryService;
import com.example.newsservice.service.NewsService;
import com.example.newsservice.service.UserService;
import com.example.newsservice.web.model.request.NewsFilterRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class NewsServiceImpl extends AbstractEntityService<News, UUID, NewsRepository> implements NewsService {

    private final UserService userService;

    private final CategoryService categoryService;

    public NewsServiceImpl(NewsRepository repository, UserService userService, CategoryService categoryService) {
        super(repository);
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @Override
    protected News updateFields(News oldEntity, News newEntity) {
        if (StringUtils.hasText(newEntity.getTitle())) {
            oldEntity.setTitle(newEntity.getTitle());
        }
        if (StringUtils.hasText(newEntity.getDescription())) {
            oldEntity.setDescription(newEntity.getDescription());
        }
        if (StringUtils.hasText(newEntity.getBody())) {
            oldEntity.setBody(newEntity.getBody());
        }
        if (newEntity.getCategory() != null &&
                !Objects.equals(oldEntity.getCategory().getId(), newEntity.getCategory().getId())) {
            oldEntity.setCategory(newEntity.getCategory());
        }

        return oldEntity;
    }

    @Override
    @Transactional
    public News addNews(News news, UUID userId, UUID categoryId) {
        User author = userService.findById(userId);
        Category category = categoryService.findById(categoryId);

        author.addNews(news);
        category.addNews(news);

        return save(news);
    }

    @Override
    @Transactional
    public News updateNews(News news, UUID id, UUID categoryId) {
        if (categoryId != null) {
            Category category = categoryService.findById(categoryId);
            news.setCategory(category);
        }

        return update(id, news);
    }

    @Override
    public Page<News> findAllByCategoryId(UUID categoryId, Pageable pageable) {
        return repository.findAllByCategoryId(categoryId, pageable);
    }

    @Override
    public Page<News> findAllByAuthorId(UUID userId, Pageable pageable) {
        return repository.findAllByAuthorId(userId, pageable);
    }

    @Override
    public Page<News> filterBy(NewsFilterRequest filter) {
        return repository.findAll(
                NewsSpecification.withFilter(filter),
                filter.getPagination().pageRequest()
        );
    }

    @Override
    public boolean existsByIdAndAuthorId(UUID id, UUID authorId) {
        return repository.existsByIdAndAuthorId(id, authorId);
    }
}
