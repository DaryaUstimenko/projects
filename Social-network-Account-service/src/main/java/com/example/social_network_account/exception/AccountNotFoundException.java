package com.example.social_network_account.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class AccountNotFoundException extends RuntimeException {

    private final HttpStatus httpStatusCode;

    public AccountNotFoundException(String message, HttpStatus httpStatusCode) {
        super(message);
        this.httpStatusCode = httpStatusCode;
    }
}
