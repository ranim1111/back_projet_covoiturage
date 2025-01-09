package com.example.projectspring.exception;

public class UserNotFoundException extends RuntimeException {

    // Constructor to pass a custom error message
    public UserNotFoundException(String message) {
        super(message);
    }

    // Optionally, you can add a constructor for a cause (nested exception)
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
