package com.example.newsservice.service;

import com.example.newsservice.entity.User;

import java.util.UUID;

public interface UserService extends EntityService<User, UUID> {

    User findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
