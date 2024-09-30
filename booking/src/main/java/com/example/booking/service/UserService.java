package com.example.booking.service;

import com.example.booking.entity.User;

import java.util.UUID;

public interface UserService extends EntityService<User, UUID> {

    User findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
