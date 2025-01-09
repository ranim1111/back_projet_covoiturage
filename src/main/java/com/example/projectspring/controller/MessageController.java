package com.example.projectspring.controller;

import com.example.projectspring.dto.MessageDTO;
import com.example.projectspring.entity.Users;
import com.example.projectspring.repository.UserRepository;
import com.example.projectspring.service.AuthService;
import com.example.projectspring.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageService messageService;

    @Autowired
    private AuthService authService;  // Inject AuthService to handle JWT extraction and user validation

    @PostMapping("/send")
    @PreAuthorize("hasRole('ROLE_CONDUCTEUR') or hasRole('ROLE_PASSAGER')")
    public ResponseEntity<?> sendMessage(@RequestBody MessageDTO messageDto, @RequestHeader("Authorization") String token) {
        try {
            // Extract JWT token (remove "Bearer " prefix)
            String jwtToken = token.substring(7);

            // Get user from JWT token using AuthService (method should validate and return user)
            Users sender = authService.getUserFromToken(jwtToken);

            if (sender == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
            }

            // Set sender's ID in the MessageDTO
            messageDto.setSenderId(sender.getId());

            // Log the messageDto to check the contents
            System.out.println("MessageDTO: " + messageDto);

            // Send the message using the MessageService
            MessageDTO sentMessage = messageService.sendMessage(messageDto);
            return ResponseEntity.ok(sentMessage);

        } catch (Exception e) {
            e.printStackTrace(); // Log the exception to the console
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }
    @PreAuthorize("hasRole('ROLE_CONDUCTEUR') or hasRole('ROLE_PASSAGER')")
    @PutMapping("/mark-as-read/{messageId}")
    public ResponseEntity<?> markAsRead(@PathVariable Long messageId, @RequestHeader("Authorization") String token) {
        try {
        	// Extract JWT token (remove "Bearer " prefix)
            String jwtToken = token.substring(7);

            // Get user from JWT token using AuthService (method should validate and return user)
            Users sender = authService.getUserFromToken(jwtToken);

            if (sender == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
            }
            MessageDTO updatedMessage = messageService.markAsRead(messageId);
            return ResponseEntity.ok(updatedMessage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while marking the message as read");
        }
    }

    @PreAuthorize("hasRole('ROLE_CONDUCTEUR') or hasRole('ROLE_PASSAGER')")
    @GetMapping("/conversation")
    public ResponseEntity<List<MessageDTO>> getMessagesBetweenUsers(
            @RequestParam Long senderId,
            @RequestParam Long receiverId,
            @RequestHeader("Authorization") String token
    ) {
        try {
            // Extract JWT token (remove "Bearer " prefix)
            String jwtToken = token.substring(7);

            // Get user from JWT token using AuthService (method should validate and return user)
            Users user = authService.getUserFromToken(jwtToken);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }

            // Fetch messages exchanged between the two users
            List<MessageDTO> messages = messageService.getMessagesBetweenUsers(senderId, receiverId);
            return ResponseEntity.ok(messages);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MessageDTO>> getUserMessages(
            @PathVariable Long userId,
            @RequestHeader("Authorization") String token
    ) {
        try {
            // Extract JWT token (remove "Bearer " prefix)
            String jwtToken = token.substring(7);

            // Get user from JWT token using AuthService (method should validate and return user)
            Users user = authService.getUserFromToken(jwtToken);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }

            // Fetch all messages for the specific user
            List<MessageDTO> userMessages = messageService.getUserMessages(userId);
            return ResponseEntity.ok(userMessages);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @PreAuthorize("hasRole('ROLE_CONDUCTEUR') or hasRole('ROLE_PASSAGER')")
    @DeleteMapping("/delete/{messageId}")
    public ResponseEntity<?> deleteMessage(@PathVariable Long messageId, @RequestHeader("Authorization") String token) {
        try {
            // Extract JWT token (remove "Bearer " prefix)
            String jwtToken = token.substring(7);

            // Get user from JWT token using AuthService (method should validate and return user)
            Users user = authService.getUserFromToken(jwtToken);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
            }

            // Check if the message belongs to the current user (sender)
            MessageDTO message = messageService.getUserMessages(user.getId())
                    .stream()
                    .filter(m -> m.getId().equals(messageId))
                    .findFirst()
                    .orElse(null);

            if (message == null || !message.getSenderId().equals(user.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only delete your own messages");
            }

            messageService.deleteMessage(messageId);
            return ResponseEntity.ok("Message deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting message");
        }
    }

    @PreAuthorize("hasRole('ROLE_CONDUCTEUR') or hasRole('ROLE_PASSAGER')")
    @PutMapping("/update/{messageId}")
    public ResponseEntity<?> updateMessage(@PathVariable Long messageId, @RequestBody MessageDTO messageDTO, @RequestHeader("Authorization") String token) {
        try {
            // Extract JWT token (remove "Bearer " prefix)
            String jwtToken = token.substring(7);

            // Get user from JWT token using AuthService (method should validate and return user)
            Users user = authService.getUserFromToken(jwtToken);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
            }

            // Check if the message belongs to the current user (sender)
            MessageDTO message = messageService.getUserMessages(user.getId())
                    .stream()
                    .filter(m -> m.getId().equals(messageId))
                    .findFirst()
                    .orElse(null);

            if (message == null || !message.getSenderId().equals(user.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only delete your own messages");
            }


            // Proceed with update
            MessageDTO updatedMessage = messageService.updateMessage(messageId, messageDTO);
            return ResponseEntity.ok(updatedMessage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating message");
        }}
}