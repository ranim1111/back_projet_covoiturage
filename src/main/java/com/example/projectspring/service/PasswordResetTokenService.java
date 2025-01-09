package com.example.projectspring.service;

import com.example.projectspring.entity.Users;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class PasswordResetTokenService {

    private final Map<String, TokenData> tokenStore = new HashMap<>();

    public String createPasswordResetToken(Users user) {
        String token = UUID.randomUUID().toString();
        tokenStore.put(token, new TokenData(user.getEmail(), LocalDateTime.now().plusMinutes(15))); // Token valide pendant 15 minutes
        return token;
    }

    public boolean validatePasswordResetToken(String token) {
        TokenData tokenData = tokenStore.get(token);
        if (tokenData == null || tokenData.getExpiryTime().isBefore(LocalDateTime.now())) {
            return false;
        }
        return true;
    }

    public String getEmailByToken(String token) {
        TokenData tokenData = tokenStore.get(token);
        return (tokenData != null) ? tokenData.getEmail() : null;
    }

    private static class TokenData {
        private final String email;
        private final LocalDateTime expiryTime;

        public TokenData(String email, LocalDateTime expiryTime) {
            this.email = email;
            this.expiryTime = expiryTime;
        }

        public String getEmail() {
            return email;
        }

        public LocalDateTime getExpiryTime() {
            return expiryTime;
        }
    }
}
