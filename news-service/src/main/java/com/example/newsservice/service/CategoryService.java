package com.example.newsservice.service;

import com.example.newsservice.entity.Category;

import java.util.UUID;

public interface CategoryService extends EntityService<Category, UUID> {

    Category findByName(String name);
}
