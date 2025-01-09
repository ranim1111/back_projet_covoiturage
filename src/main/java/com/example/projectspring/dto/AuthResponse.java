package com.example.projectspring.dto;

public class AuthResponse {
    private String token;
    private String type;
    private String role;
    private String username;
    private String email;
    private String phoneNumber;
    private Long id;
    private String expiration;
    private String message;  // Optional error message field

    // Constructor for successful authentication response
    public AuthResponse(String token, String type, String role, String username, String email,
                        String phoneNumber, Long id, String expiration) {
        this.token = token;
        this.type = type;
        this.role = role;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.id = id;
        this.expiration = expiration;
    }

    // Constructor for error messages
    public AuthResponse(String message) {
        this.message = message;
    }

    // Getters and setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
