package com.example.projectspring.service;


import org.springframework.stereotype.Service;

@Service
public interface EmailService  {
    String sendMail(String to, String subject, String body);
}
