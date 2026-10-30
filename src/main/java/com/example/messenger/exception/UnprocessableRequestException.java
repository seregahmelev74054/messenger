package com.example.messenger.exception;

public class UnprocessableRequestException extends RuntimeException {
    public UnprocessableRequestException(String message) {
        super(message);
    }
}
