package com.example.booking.exception;

public class WrongDatePeriodException extends RuntimeException {
    public WrongDatePeriodException(String message) {
        super(message);
    }
}
