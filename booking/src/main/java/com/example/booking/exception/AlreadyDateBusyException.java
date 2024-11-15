package com.example.booking.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AlreadyDateBusyException extends ConstraintViolationException {

    public AlreadyDateBusyException(String message) {
        super(message, null);
    }
}
