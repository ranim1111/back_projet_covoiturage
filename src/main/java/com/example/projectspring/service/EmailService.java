package com.example.projectspring.service;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;

@Service
public interface EmailService  {
    String sendMail(String to, String subject, String body);

    void sendReservationStatusEmail(String email, String subject, String messageBody);  // Ajout de la méthode
}
