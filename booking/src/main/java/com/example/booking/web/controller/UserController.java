package com.example.booking.web.controller;

import com.example.booking.aop.AuthorizeAction;
import com.example.booking.entity.RoleType;
import com.example.booking.entity.User;
import com.example.booking.exception.AlreadyExistsException;
import com.example.booking.mapper.UserMapper;
import com.example.booking.service.UserService;
import com.example.booking.service.impl.KafkaEventService;
import com.example.booking.web.model.request.PaginationRequest;
import com.example.booking.web.model.request.UpsertUserRequest;
import com.example.booking.web.model.response.ModelListResponse;
import com.example.booking.web.model.response.UserResponse;
import com.example.booking.web.model.response.UserUpdateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    private final KafkaEventService kafkaEventService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<UserResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(userMapper.userToResponse(userService.findById(id)));
    }

    @GetMapping("/name/{username}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
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

        User savedUser = userService.save(newUser);
        kafkaEventService.registrationEvent(savedUser.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userMapper.userToResponse(savedUser));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @AuthorizeAction(actionType = "update")
    public ResponseEntity<UserUpdateResponse> updateUser(@PathVariable UUID id, @RequestBody UpsertUserRequest request) {
        User updatedUser = userService.update(id, userMapper.upsertRequestToUser(request));

        return ResponseEntity.ok(userMapper.userUpdateToResponse(updatedUser));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @AuthorizeAction(actionType = "delete")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        userService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

}
