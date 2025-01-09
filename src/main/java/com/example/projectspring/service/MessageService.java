package com.example.projectspring.service;

import com.example.projectspring.dto.MessageDTO;
import java.util.List;

public interface MessageService {
    MessageDTO sendMessage(MessageDTO messageDTO);
    List<MessageDTO> getMessagesBetweenUsers(Long senderId, Long receiverId);
    List<MessageDTO> getUserMessages(Long userId);
    MessageDTO markAsRead(Long messageId);
    MessageDTO updateMessage(Long messageId, MessageDTO messageDTO);  // Added update method
    void deleteMessage(Long messageId);  // Added delete method
}
