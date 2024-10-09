package com.example.booking.service.impl;

import com.example.booking.entity.User;
import com.example.booking.exception.AlreadyExistsException;
import com.example.booking.repository.UserRepository;
import com.example.booking.service.AbstractEntityService;
import com.example.booking.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl extends AbstractEntityService<User, UUID, UserRepository>
        implements UserService {

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder) {
        super(repository);
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findById(UUID id) {
        return repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(MessageFormat.format("User with id {0} not found!", id)));
    }

    @Override
    public User findByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(
                        MessageFormat.format("User with username {0} not found!", username)
                ));
    }

    @Override
    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public User save(User entity) {
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        return super.save(entity);
    }

    @Override
    protected User updateFields(User oldEntity, User newEntity) {
        if (!Objects.equals(oldEntity.getUsername(), newEntity.getUsername()) && existsByUsername(newEntity.getUsername())) {
            throw new AlreadyExistsException(
                    MessageFormat.format("User with username {0} already exists!", newEntity.getUsername())
            );
        } else if (!Objects.equals(oldEntity.getUsername(), newEntity.getUsername())) {
            oldEntity.setUsername(newEntity.getUsername());
        }

        if (!Objects.equals(oldEntity.getEmail(), newEntity.getEmail()) && existsByEmail(newEntity.getEmail())) {
            throw new AlreadyExistsException(
                    MessageFormat.format("User with email {0} already exists!", newEntity.getUsername())
            );
        } else if (!Objects.equals(oldEntity.getEmail(), newEntity.getEmail())) {
            oldEntity.setEmail(newEntity.getEmail());
        }
        log.info("UpdateFields for user: " + oldEntity.getUsername());

        return oldEntity;
    }
}
