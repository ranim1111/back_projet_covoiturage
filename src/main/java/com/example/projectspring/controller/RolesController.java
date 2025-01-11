package com.example.projectspring.controller;

import com.example.projectspring.entity.Roles;
import com.example.projectspring.service.RolesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
public class RolesController {

    private final RolesService rolesService;

    // Injection par constructeur pour une meilleure testabilité
    public RolesController(RolesService rolesService) {
        this.rolesService = rolesService;
    }

    /**
     * Récupérer tous les rôles.
     */
    @GetMapping
    public ResponseEntity<?> getAllRoles() {
        return ResponseEntity.ok(rolesService.getAllRoles());
    }

    /**
     * Récupérer un rôle par son nom.
     */
    @GetMapping("/{name}")
    public ResponseEntity<Object> getRoleByName(@PathVariable String name) {
        Optional<Roles> role = rolesService.getRoleByName(name);

        if (role.isPresent()) {
            return ResponseEntity.ok(role.get()); // Retourne l'objet Roles si trouvé
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Role not found"); // Retourne un message d'erreur si non trouvé
        }
    }

    /**
     * Créer un nouveau rôle.
     */
    @PostMapping
    public ResponseEntity<?> createRole(@RequestBody Roles role) {
        Roles newRole = rolesService.createRole(role);
        if (newRole == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Role already exists");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(newRole);
    }

    /**
     * Mettre à jour un rôle existant.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRole(@PathVariable Integer id, @RequestBody Roles role) {
        Roles updatedRole = rolesService.updateRole(id, role);
        if (updatedRole == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Role not found");
        }
        return ResponseEntity.ok(updatedRole);
    }

    /**
     * Supprimer un rôle par son ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable Integer id) {
        boolean isDeleted = rolesService.deleteRole(id);
        if (!isDeleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Role not found");
        }
        return ResponseEntity.ok("Role deleted");
    }
}
