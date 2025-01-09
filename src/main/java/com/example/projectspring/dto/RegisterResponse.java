package com.example.projectspring.dto;

import com.example.projectspring.entity.Users;

public class RegisterResponse {
	private boolean success;
    private String message;
    private Users user; // The registered user object
    
    
    public RegisterResponse(boolean success, String message, Users user) {
        this.success = success;
        this.message = message;
        this.user = user;
    }
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Users getUser() {
		return user;
	}
	public void setUser(Users user) {
		this.user = user;
	}
}
