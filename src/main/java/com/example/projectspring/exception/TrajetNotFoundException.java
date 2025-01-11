package com.example.projectspring.exception;

public class TrajetNotFoundException extends RuntimeException {

    public TrajetNotFoundException(String message) {
        super(message);
    }

    public TrajetNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
