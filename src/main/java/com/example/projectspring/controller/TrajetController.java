package com.example.projectspring.controller;

import com.example.projectspring.entity.Trajet;
import com.example.projectspring.entity.Users;
import com.example.projectspring.exception.TrajetNotFoundException;
import com.example.projectspring.service.AuthService;
import com.example.projectspring.service.TrajetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/trajets")
public class TrajetController {

    @Autowired
    private TrajetService trajetService;
    @Autowired
    private AuthService authService; 

    @PostMapping("/{conducteurId}")
    @PreAuthorize("hasRole('ROLE_CONDUCTEUR')")  
    public ResponseEntity<Trajet> ajouterTrajet(
            @PathVariable Long conducteurId,
            @RequestBody Trajet trajet,
            @RequestHeader("Authorization") String token,
            Authentication authentication
    ) {
        try {
         
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }


            String jwtToken = token.substring(7);  
            

            Users user = authService.getUserFromToken(jwtToken); 
            
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            
           
            if (!user.getId().equals(conducteurId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Forbidden si les IDs ne correspondent pas
            }

            
            Users conducteur = new Users();
            conducteur.setId(conducteurId);  
            trajet.setConducteur(conducteur);

            
            Trajet savedTrajet = trajetService.ajouterTrajet(trajet);

           
            return ResponseEntity.status(HttpStatus.CREATED).body(savedTrajet);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    @GetMapping
    @PreAuthorize("hasRole('ROLE_CONDUCTEUR') or hasRole('ROLE_PASSAGER')")
    public ResponseEntity<List<Trajet>> getAllTrajets(
            @RequestHeader("Authorization") String token,
            Authentication authentication
    ) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            String jwtToken = token.substring(7);  

            Users user = authService.getUserFromToken(jwtToken);  
            
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            List<Trajet> trajets = trajetService.getAllTrajets();
            if (trajets.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(trajets);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CONDUCTEUR') or hasRole('ROLE_PASSAGER')")
    public ResponseEntity<Trajet> getTrajetById(
            @PathVariable Integer id,
            @RequestHeader("Authorization") String token,
            Authentication authentication
    ) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            String jwtToken = token.substring(7);

            Users user = authService.getUserFromToken(jwtToken);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            Trajet trajet = trajetService.getTrajetById(id)
                    .orElseThrow(() -> new TrajetNotFoundException("Trajet non trouvé avec l'ID: " + id));
            
            return ResponseEntity.ok(trajet);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/conducteur/{conducteurId}")
    @PreAuthorize("hasRole('ROLE_CONDUCTEUR')")
    public ResponseEntity<?> getTrajetsByConducteur(
            @PathVariable Integer conducteurId,
            @RequestHeader("Authorization") String token,
            Authentication authentication
    ) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Collections.singletonMap("message", "Utilisateur non authentifié"));
            }

            String jwtToken = token.substring(7);

            Users user = authService.getUserFromToken(jwtToken);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Collections.singletonMap("message", "Token d'authentification invalide"));
            }



            List<Trajet> trajets = trajetService.getTrajetsByConducteur(conducteurId);
            if (trajets.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(trajets);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Erreur interne du serveur"));
        }
    }

    @GetMapping("/passager/{passagerId}")
    @PreAuthorize("hasRole('ROLE_PASSAGER')")
    public ResponseEntity<?> getTrajetsByPassager(
            @PathVariable Integer passagerId,
            @RequestHeader("Authorization") String token,
            Authentication authentication
    ) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Collections.singletonMap("message", "Utilisateur non authentifié"));
            }

            String jwtToken = token.substring(7);

            Users user = authService.getUserFromToken(jwtToken);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Collections.singletonMap("message", "Token d'authentification invalide"));
            }

            if (!user.getId().equals(passagerId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Collections.singletonMap("message", "Accès interdit pour ce passager"));
            }

            List<Trajet> trajets = trajetService.getTrajetsByPassager(passagerId);
            if (trajets.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(trajets);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Erreur interne du serveur"));
        }
    }

    @PutMapping("/{id}/conducteur/{conducteurId}")
    @PreAuthorize("hasRole('ROLE_CONDUCTEUR')")
    public ResponseEntity<?> updateTrajet(
            @PathVariable Integer id,
            @PathVariable Integer conducteurId,
            @RequestBody Trajet trajet,
            @RequestHeader(value = "Authorization") String token,
            Authentication authentication
    ) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Collections.singletonMap("message", "Utilisateur non authentifié"));
            }

            String jwtToken = token.substring(7); // Assuming the token starts with 'Bearer '
            Users user = authService.getUserFromToken(jwtToken);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Collections.singletonMap("message", "Token d'authentification invalide"));
            }

            // Debugging: Print the user ID and conducteur ID
            System.out.println("ID de l'utilisateur authentifié : " + user.getId());
            System.out.println("ID du conducteur dans l'URL : " + conducteurId);

   

            if (trajet == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("message", "Trajet invalide"));
            }

            // Attempt to update the trajet
            Optional<Trajet> updatedTrajetOpt = trajetService.updateTrajet(id, trajet);

            if (updatedTrajetOpt.isPresent()) {
                return ResponseEntity.ok(updatedTrajetOpt.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("message", "Trajet non trouvé pour la mise à jour avec l'ID: " + id));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Erreur interne du serveur"));
        }
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CONDUCTEUR')")
    public ResponseEntity<?> deleteTrajet(
            @PathVariable Integer id,
            @RequestHeader(value = "Authorization", required = false) String token, // Token can be missing
            Authentication authentication
    ) {
        try {
            // Step 1: Check if the token is missing
            if (token == null || token.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Collections.singletonMap("message", "Token d'authentification manquant"));
            }

            // Step 2: Check if the token is valid and starts with "Bearer "
            if (!token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Collections.singletonMap("message", "Token d'authentification invalide"));
            }

            
            String jwtToken = token.substring(7);

            
            Users user = authService.getUserFromToken(jwtToken);

            
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Collections.singletonMap("message", "Token d'authentification invalide"));
            }

           
            Optional<Trajet> trajetOptional = trajetService.getTrajetById(id);

            
            if (!trajetOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("message", "Trajet non trouvé avec l'ID: " + id));
            }

          
            Trajet trajet = trajetOptional.get();
            if (!trajet.getConducteur().getId().equals(user.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Collections.singletonMap("message", "Accès interdit : Vous n'êtes pas le conducteur de ce trajet"));
            }

        
            boolean deleted = trajetService.deleteTrajet(id);

 
            if (!deleted) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("message", "Trajet non trouvé avec l'ID: " + id));
            }

     
            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Erreur interne du serveur"));
        }
    }

}
