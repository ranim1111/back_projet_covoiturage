package com.example.projectspring.service;

import com.example.projectspring.dto.MessageDTO;
import com.example.projectspring.entity.Message;
import com.example.projectspring.entity.Users;
import com.example.projectspring.repository.MessageRepository;
import com.example.projectspring.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public MessageDTO sendMessage(MessageDTO messageDTO) {
        Users sender = userRepository.findById(messageDTO.getSenderId()).orElseThrow(() -> new RuntimeException("Sender not found"));
        Users receiver = userRepository.findById(messageDTO.getReceiverId()).orElseThrow(() -> new RuntimeException("Receiver not found"));

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(messageDTO.getContent());
        message.setSentAt(LocalDateTime.now());
        message.setRead(false);

        Message savedMessage = messageRepository.save(message);

        return toDTO(savedMessage);
    }
    public MessageDTO markAsRead(Long messageId) {
        try {
            // Retrieve the message from the repository
            Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

            // Mark the message as read
            message.setRead(true);
            messageRepository.save(message);

            // Extract the necessary details for the DTO
            Long senderId = message.getSender().getId(); // Get sender's ID
            Long receiverId = message.getReceiver().getId(); // Get receiver's ID
            String content = message.getContent(); // Message content
            LocalDateTime sentAt = message.getSentAt(); // Sent time
            boolean isRead = message.isRead(); // Read status

            // Return the updated MessageDTO
            return new MessageDTO(message.getId(), senderId, receiverId, content, sentAt, isRead);
        } catch (Exception e) {
            throw new RuntimeException("Error marking message as read", e);
        }
    }
    
    @Override
    public List<MessageDTO> getMessagesBetweenUsers(Long senderId, Long receiverId) {
        // Fetch messages exchanged between the two users
        List<Message> messages = messageRepository.findBySenderIdAndReceiverIdOrSenderIdAndReceiverId(senderId, receiverId, receiverId, senderId);

        // Convert Message entities to MessageDTOs
        return messages.stream()
                .map(message -> new MessageDTO(
                        message.getId(),                               // Ensure this is a Long
                        message.getSender().getId(),                    // Get the sender's ID
                        message.getReceiver().getId(),                  // Get the receiver's ID
                        message.getContent(),                           // Get the content of the message
                        message.getSentAt(),                            // Get the sent timestamp
                        message.isRead()                                // Get the read status
                ))
                .collect(Collectors.toList());
    }




    @Override
    public List<MessageDTO> getUserMessages(Long userId) {
        List<Message> messages = messageRepository.findBySenderIdOrReceiverId(userId, userId);
        return messages.stream().map(this::toDTO).collect(Collectors.toList());
    }
    @Override
    public MessageDTO updateMessage(Long messageId, MessageDTO messageDTO) {
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new RuntimeException("Message not found"));
        message.setContent(messageDTO.getContent());
        message.setSentAt(LocalDateTime.now());
        messageRepository.save(message);
        return toDTO(message);
    }

    @Override
    public void deleteMessage(Long messageId) {
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new RuntimeException("Message not found"));
        messageRepository.delete(message);
    }
    private MessageDTO toDTO(Message message) {
        return new MessageDTO(
                message.getId(),
                message.getSender().getId(),
                message.getReceiver().getId(),
                message.getContent(),
                message.getSentAt(),
                message.isRead()
        );
    }
}
