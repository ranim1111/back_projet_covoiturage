package com.example.projectspring.service;

import com.example.projectspring.entity.Roles;
import com.example.projectspring.repository.RolesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RolesService {

    private final RolesRepository rolesRepository;

    // Injection par constructeur
    public RolesService(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    /**
     * Créer un nouveau rôle.
     */
    public Roles createRole(Roles role) {
        if (rolesRepository.findRoleByName(role.getName()) != null) {
            return null; // Le rôle existe déjà
        }
        return rolesRepository.save(role);
    }

    /**
     * Récupérer tous les rôles.
     */
    public List<Roles> getAllRoles() {
        return rolesRepository.findAll();
    }

    /**
     * Récupérer un rôle par son nom.
     */
    public Optional<Roles> getRoleByName(String name) {
        return Optional.ofNullable(rolesRepository.findRoleByName(name));
    }

    /**
     * Mettre à jour un rôle existant.
     */
    public Roles updateRole(Integer id, Roles role) {
        if (!rolesRepository.existsById(id)) {
            return null; // Le rôle n'existe pas
        }
        role.setId(id);
        return rolesRepository.save(role);
    }

    /**
     * Supprimer un rôle.
     */
    public boolean deleteRole(Integer id) {
        if (!rolesRepository.existsById(id)) {
            return false; // Le rôle n'existe pas
        }
        rolesRepository.deleteById(id);
        return true;
    }
}
