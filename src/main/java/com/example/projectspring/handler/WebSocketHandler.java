package com.example.projectspring.handler;

import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class WebSocketHandler extends TextWebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Vérification de l'authentification via le token JWT
        String token = getJwtTokenFromSession(session); // Récupérer le token depuis les en-têtes ou la session

        if (token != null && isValidToken(token)) {
            // Si le token est valide, associer l'authentification à la session WebSocket
            UsernamePasswordAuthenticationToken authentication = authenticateWithJwt(token);
            session.getAttributes().put("authentication", authentication);
            session.sendMessage(new TextMessage("Connexion réussie"));
        } else {
            session.close(CloseStatus.NOT_ACCEPTABLE); // Ferme la connexion si le token est invalide
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Gérer la réception des messages
        String receivedMessage = message.getPayload();
        System.out.println("Message reçu: " + receivedMessage);

        // Traitement et réponse
        session.sendMessage(new TextMessage("Message traité: " + receivedMessage));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // Actions après la fermeture de la connexion
        System.out.println("Connexion fermée");
    }

    // Méthodes d'assistance

    private String getJwtTokenFromSession(WebSocketSession session) {
        // Récupérer le token JWT depuis les en-têtes ou les paramètres
        // Exemple : session.getUri().getQueryParameter("token");
        return session.getUri().getQuery();
    }

    private boolean isValidToken(String token) {
        // Implémentez la logique de validation du token JWT (décryptage, vérification de la signature, etc.)
        return true; // Remplacez par votre logique de validation JWT
    }

    private UsernamePasswordAuthenticationToken authenticateWithJwt(String token) {
        // Implémentez la logique pour extraire les informations d'utilisateur du token et authentifier l'utilisateur
        return new UsernamePasswordAuthenticationToken("user", null); // Exemple d'authentification fictive
    }
}
