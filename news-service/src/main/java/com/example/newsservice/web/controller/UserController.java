package com.example.newsservice.web.controller;

import com.example.newsservice.entity.User;
import com.example.newsservice.exception.AlreadyExistsException;
import com.example.newsservice.mapper.UserMapper;
import com.example.newsservice.service.UserService;
import com.example.newsservice.web.model.request.PaginationRequest;
import com.example.newsservice.web.model.request.UpsertUserRequest;
import com.example.newsservice.web.model.response.ModelListResponse;
import com.example.newsservice.web.model.response.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<ModelListResponse<UserResponse>> getAllUsersPage(@Valid PaginationRequest request) {
        Page<User> userPage = userService.findAll(request.pageRequest());

        return ResponseEntity.ok(
                ModelListResponse.<UserResponse>builder()
                        .totalCount(userPage.getTotalElements())
                        .data(userPage.stream().map(userMapper::userToResponse).toList())
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(userMapper.userToResponse(userService.findById(id)));
    }

    @GetMapping("/name/{username}")
    public ResponseEntity<UserResponse> getUserByName(@PathVariable String username){
        return ResponseEntity.ok(userMapper.userToResponse(userService.findByUsername(username)));
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody UpsertUserRequest request) {
        if (userService.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException(MessageFormat.format("User with email {0} already exists!", request.getEmail()));
        }
        if (userService.existsByUsername(request.getUsername())) {
            throw new AlreadyExistsException(MessageFormat.format("User with name {0} already exists!", request.getUsername()));
        }
        User newUser = userMapper.upsertRequestToUser(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userMapper.userToResponse(userService.save(newUser)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@RequestBody UpsertUserRequest request, @PathVariable UUID id) {
        User updatedUser = userService.update(id, userMapper.upsertRequestToUser(request));

        return ResponseEntity.ok(userMapper.userToResponse(updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        userService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

}
