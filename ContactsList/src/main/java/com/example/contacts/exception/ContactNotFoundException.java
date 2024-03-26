package com.example.contacts.exception;

public class ContactNotFoundException extends RuntimeException{
    public ContactNotFoundException(String s) {
    }
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
