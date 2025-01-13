package com.example.projectspring.controller;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebSocketRestController {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketRestController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/send-message")
    public void sendMessage(@RequestBody String message) {
        // Send a message to all clients subscribed to /topic/messages
        messagingTemplate.convertAndSend("/topic/messages", message);
    }
}
