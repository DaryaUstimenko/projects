package com.example.newsservice.service.impl;

import com.example.newsservice.entity.Category;
import com.example.newsservice.exception.EntityNotFoundException;
import com.example.newsservice.repository.CategoryRepository;
import com.example.newsservice.service.AbstractEntityService;
import com.example.newsservice.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class CategoryServiceImpl extends AbstractEntityService<Category, UUID, CategoryRepository>
        implements CategoryService {

    public CategoryServiceImpl(CategoryRepository repository) {
        super(repository);
    }

    @Override
    protected Category updateFields(Category oldEntity, Category newEntity) {
        if (!Objects.equals(oldEntity.getName(), newEntity.getName())) {
            oldEntity.setName(newEntity.getName());
        }

        return oldEntity;
    }

    @Override
    public Category findByName(String name) {
        return repository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException(
                        MessageFormat.format("Category with name {0} not found!", name)
                ));
    }
}
