package com.example.projectspring.handler;

import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.CloseStatus;

import java.util.HashMap;
import java.util.Map;

public class ChatHandler extends TextWebSocketHandler {

    private Map<Long, WebSocketSession> userSessions = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Long userId = getUserIdFromSession(session); // Implémentez cette méthode pour récupérer l'ID de l'utilisateur
        userSessions.put(userId, session);
        System.out.println("User connected: " + userId);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Long senderId = getUserIdFromSession(session); // Implémentez cette méthode pour récupérer l'ID de l'utilisateur
        String payload = message.getPayload();

        // Logique pour envoyer un message à un autre utilisateur (par exemple, à un conducteur ou un passager)
        Long receiverId = getReceiverId(payload); // Implémentez cette méthode pour extraire le destinataire du message

        WebSocketSession receiverSession = userSessions.get(receiverId);
        if (receiverSession != null && receiverSession.isOpen()) {
            receiverSession.sendMessage(new TextMessage(payload));
        } else {
            session.sendMessage(new TextMessage("User not available"));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Long userId = getUserIdFromSession(session);
        userSessions.remove(userId);
        System.out.println("User disconnected: " + userId);
    }

    private Long getUserIdFromSession(WebSocketSession session) {
        // Implémentez cette méthode pour extraire l'ID de l'utilisateur depuis la session WebSocket
        return 123L; // Remplacez cette ligne avec votre logique de récupération de l'ID utilisateur
    }

    private Long getReceiverId(String messagePayload) {
        // Implémentez cette méthode pour extraire l'ID du destinataire à partir du message
        return 456L; // Remplacez cette ligne avec votre logique de récupération de l'ID du destinataire
    }
}
