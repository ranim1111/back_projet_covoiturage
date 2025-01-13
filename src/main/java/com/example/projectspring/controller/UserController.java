package com.example.projectspring.controller;

import com.example.projectspring.entity.Users;
import com.example.projectspring.repository.UserRepository;
import com.example.projectspring.service.AuthService;
import com.example.projectspring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;  // Injection du UserService

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    // Create a new user (only admin can do this)
    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody Users user) {
        try {
            // Enregistrer l'utilisateur
            Users savedUser = userRepository.save(user);  // Sauvegarder l'utilisateur et récupérer l'utilisateur sauvegardé avec l'ID

            // Retourner le message de succès avec l'objet utilisateur
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body( savedUser); // Inclure l'objet utilisateur dans la réponse
        } catch (Exception e) {
            // Enregistrer l'exception pour déboguer
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body( e.getMessage()); // Retourner le message d'erreur
        }
    }


    // Get all users (only admin can do this)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/")
    public ResponseEntity<List<Users>> getAllUsers() {
        try {
            // Fetch all users
            List<Users> users = userRepository.findAll();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            // Log the exception and return a 500 status
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Get user by ID (only admin can do this)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable Long id) {
        try {
            // Fetch user by ID
            Users user = userRepository.findById(id).orElse(null);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Update user (only admin can do this)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody Users userDetails) {
        try {
            // Trouver l'utilisateur par ID
            Users user = userRepository.findById(id).orElse(null);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            // Mettre à jour les détails de l'utilisateur
            user.setFirstName(userDetails.getFirstName());
            user.setLastName(userDetails.getLastName());
            user.setEmail(userDetails.getEmail());
            user.setPhoneNumber(userDetails.getPhoneNumber());
            user.setUsername(userDetails.getUsername());
            user.setPassword(userDetails.getPassword());  // Vous pouvez vouloir hacher le mot de passe ici
            user.setVille(userDetails.getVille());
            user.setRole(userDetails.getRole());
            user.setNaturePersonne(userDetails.getNaturePersonne());
            user.setBehaviorReported(userDetails.getBehaviorReported());

            // Sauvegarder l'utilisateur mis à jour
            Users updatedUser = userRepository.save(user);

            // Retourner le message de succès avec l'objet utilisateur mis à jour
            return ResponseEntity.status(HttpStatus.OK)
                    .body( updatedUser);  // Inclure l'objet utilisateur mis à jour dans la réponse
        } catch (Exception e) {
            // Enregistrer l'exception pour déboguer
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body( e.getMessage());  // Retourner le message d'erreur
        }
    }


    // Delete user (only admin can do this)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            // Find the user by ID
            Users user = userRepository.findById(id).orElse(null);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            // Delete the user
            userRepository.delete(user);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting user: " + e.getMessage());
        }
    }
}