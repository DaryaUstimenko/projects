package com.example.booking.web.controller;

import com.example.booking.entity.RoleType;
import com.example.booking.entity.User;
import com.example.booking.exception.AlreadyExistsException;
import com.example.booking.mapper.UserMapper;
import com.example.booking.service.UserService;
import com.example.booking.web.model.request.UpsertUserRequest;
import com.example.booking.web.model.response.UserResponse;
import com.example.booking.web.model.response.UserUpdateResponse;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(userMapper.userToResponse(userService.findById(id)));
    }

    @GetMapping("/name/{username}")
    public ResponseEntity<UserResponse> getUserByName(@PathVariable String username) {
        return ResponseEntity.ok(userMapper.userToResponse(userService.findByUsername(username)));
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody UpsertUserRequest request,
                                                   @RequestParam RoleType role) {
        if (userService.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException(MessageFormat.format("User with email {0} already exists!", request.getEmail()));
        }
        if (userService.existsByUsername(request.getUsername())) {
            throw new AlreadyExistsException(MessageFormat.format("User with name {0} already exists!", request.getUsername()));
        }
        User newUser = userMapper.upsertRequestToUser(request);
        newUser.addRole(role);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userMapper.userToResponse(userService.save(newUser)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserUpdateResponse> updateUser(@PathVariable UUID id, @RequestBody UpsertUserRequest request) {
        User updatedUser = userService.update(id, userMapper.upsertRequestToUser(request));

        return ResponseEntity.ok(userMapper.userUpdateToResponse(updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        userService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

}
