package com.example.booking.web.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateResponse {

    private UUID id;

    private String username;

    private String password;

    private String email;
}
