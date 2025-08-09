package com.example.wallet.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class WalletNotFoundException extends RuntimeException {

    private final HttpStatus httpStatusCode;

    public WalletNotFoundException(String message, HttpStatus httpStatusCode) {
        super(message);
        this.httpStatusCode = httpStatusCode;
    }
}