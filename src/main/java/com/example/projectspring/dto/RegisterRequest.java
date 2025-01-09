package com.example.projectspring.dto;

public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String username;
    private String password;
    private String ville;
    private String role; // Optional or required based on your logic
    private String naturePersonne;
    
    public RegisterRequest(String firstName, String lastName, String phoneNumber, String email, String username,
			String password, String ville, String role, String naturePersonne) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.username = username;
		this.password = password;
		this.ville = ville;
		this.role = role;
		this.naturePersonne = naturePersonne;
	}
	public String getNaturePersonne() {
		return naturePersonne;
	}
	public void setNaturePersonne(String naturePersonne) {
		this.naturePersonne = naturePersonne;
	}
	// Add getters and setters
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getVille() {
		return ville;
	}
	public void setVille(String ville) {
		this.ville = ville;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
}
