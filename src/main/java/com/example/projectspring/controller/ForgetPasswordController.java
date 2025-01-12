package com.example.projectspring.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.projectspring.entity.Users;
import com.example.projectspring.repository.UserRepository;
import com.example.projectspring.service.EmailService;


@RestController
@RequestMapping("/api/auth")
public class ForgetPasswordController {

	    
	    private final PasswordEncoder passwordEncoder;
	    @Autowired
	    private EmailService emailService;

	    @Autowired
	    private UserRepository userRepository;

	    @Autowired
	    public ForgetPasswordController(PasswordEncoder passwordEncoder) {
	        this.passwordEncoder = passwordEncoder;
	    }

	    // In-memory storage for email and reset tokens (temporary solution)
	    private final Map<String, String> resetEmailTokens = new HashMap<>(); // Stores the email for each reset token
	    private final Map<String, Long> resetTimestampTokens = new HashMap<>(); // Stores the timestamp for each reset token
	    private final long EXPIRATION_TIME = 60 * 60 * 1000; // 1 hour expiration time

	    // 1. Request Password Reset (Forgot Password)
	    @PostMapping("/forgot-password")
	    public String forgotPassword(@RequestParam String email) {
	        Users user = userRepository.findByEmail(email);
	        if (user == null) {
	            return "Email not found";
	        }

	        // Generate a unique reset token
	        String resetToken = UUID.randomUUID().toString();

	        // Store the email and timestamp separately
	        resetEmailTokens.put(resetToken, email);
	        resetTimestampTokens.put(resetToken, System.currentTimeMillis());

	        // Send the token to the user's email
	        String resetLink = "http://localhost:4200/resetpassword-page?token=" + resetToken;
	        String subject = "Password Reset Request";
	        String body = "To reset your password, click the link below:\n" + resetLink;
	        emailService.sendMail(email, subject, body);

	        return "Password reset email sent successfully!";
	    }

	    @PostMapping("/reset-password")
	    public String resetPassword(@RequestParam String token, @RequestParam String newPassword) {
	        // Retrieve the email and timestamp associated with the token
	        String email = resetEmailTokens.get(token);
	        Long timestamp = resetTimestampTokens.get(token);

	        // Check if the token is valid (email or timestamp might not exist if the token is invalid)
	        if (email == null || timestamp == null) {
	            return "Invalid or expired reset token.";
	        }

	        // Check if the token has expired
	        if (System.currentTimeMillis() - timestamp > EXPIRATION_TIME) {
	            resetEmailTokens.remove(token); // Remove expired token
	            resetTimestampTokens.remove(token); // Remove expired timestamp
	            return "Reset token has expired.";
	        }

	        // Retrieve the user by email
	        Users user = userRepository.findByEmail(email);
	        if (user == null) {
	            return "Email not found";
	        }

	        // Reset the user's password
	        user.setPassword(passwordEncoder.encode(newPassword)); // Make sure to hash the password in production
	        userRepository.save(user);

	        // Remove the token after use
	        
	        resetEmailTokens.remove(token);
	        resetTimestampTokens.remove(token);

	        return "Password has been reset successfully!";
	    }
	}