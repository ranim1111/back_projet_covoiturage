package com.example.projectspring.service;

import com.example.projectspring.entity.Users;
import com.example.projectspring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Créer un nouvel utilisateur
    public Users createUser(Users user) {
        return userRepository.save(user);
    }

    // Lire un utilisateur par ID
    public Optional<Users> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Lire tous les utilisateurs
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    // Mettre à jour un utilisateur
    @Transactional
    public Users updateUser(Long id, Users userDetails) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setEmail(userDetails.getEmail());
        user.setPhoneNumber(userDetails.getPhoneNumber());
        user.setUsername(userDetails.getUsername());
        user.setPassword(userDetails.getPassword());
        user.setVille(userDetails.getVille());
        user.setRole(userDetails.getRole());
        user.setNaturePersonne(userDetails.getNaturePersonne());
        user.setBehaviorReported(userDetails.getBehaviorReported());
        return user;
    }

    // Supprimer un utilisateur
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}