package com.example.wallet.controller;

import com.example.wallet.exception.InternalServerException;
import com.example.wallet.exception.WalletException;

import com.example.wallet.model.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerController {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> notFound(EntityNotFoundException ex) {
        log.error("Кошелек не найден", ex);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new com.example.wallet.model.ErrorResponse(ex.getLocalizedMessage()));
    }

    @ExceptionHandler(WalletException.class)
    public ResponseEntity<ErrorResponse> balanceNotEnough(WalletException ex) {
        log.error("Недостаточно средств на счету", ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<ErrorResponse> serverException(InternalServerException ex) {
        log.error("Ошибка сервера", ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(ex.getMessage()));
    }
}

