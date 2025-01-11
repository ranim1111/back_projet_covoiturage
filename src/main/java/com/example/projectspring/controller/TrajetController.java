package com.example.projectspring.controller;

import com.example.projectspring.entity.Trajet;
import com.example.projectspring.exception.TrajetNotFoundException;
import com.example.projectspring.service.TrajetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trajets")
public class TrajetController {

    @Autowired
    private TrajetService trajetService;

    // Ajouter un trajet
    @PostMapping
    public ResponseEntity<Trajet> ajouterTrajet(@RequestBody Trajet trajet) {
        if (trajet == null) {
            return ResponseEntity.badRequest().build();
        }
        Trajet savedTrajet = trajetService.ajouterTrajet(trajet);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTrajet);
    }

    // Récupérer tous les trajets
    @GetMapping
    public ResponseEntity<List<Trajet>> getAllTrajets() {
        List<Trajet> trajets = trajetService.getAllTrajets();
        if (trajets.isEmpty()) {
            return ResponseEntity.noContent().build();  // Retourne 204 si aucun trajet
        }
        return ResponseEntity.ok(trajets);
    }

    // Récupérer un trajet par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Trajet> getTrajetById(@PathVariable Integer id) {
        Trajet trajet = trajetService.getTrajetById(id)
                .orElseThrow(() -> new TrajetNotFoundException("Trajet non trouvé avec l'ID: " + id));
        return ResponseEntity.ok(trajet);
    }

    // Récupérer les trajets d'un conducteur
    @GetMapping("/conducteur/{conducteurId}")
    public ResponseEntity<List<Trajet>> getTrajetsByConducteur(@PathVariable Integer conducteurId) {
        List<Trajet> trajets = trajetService.getTrajetsByConducteur(conducteurId);
        if (trajets.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(trajets);
    }

    // Récupérer les trajets d'un passager
    @GetMapping("/passager/{passagerId}")
    public ResponseEntity<List<Trajet>> getTrajetsByPassager(@PathVariable Integer passagerId) {
        List<Trajet> trajets = trajetService.getTrajetsByPassager(passagerId);
        if (trajets.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(trajets);
    }

    // Mettre à jour un trajet
    @PutMapping("/{id}")
    public ResponseEntity<Trajet> updateTrajet(@PathVariable Integer id, @RequestBody Trajet trajet) {
        if (trajet == null) {
            return ResponseEntity.badRequest().build();
        }
        Trajet updatedTrajet = trajetService.updateTrajet(id, trajet)
                .orElseThrow(() -> new TrajetNotFoundException("Trajet non trouvé pour la mise à jour avec l'ID: " + id));
        return ResponseEntity.ok(updatedTrajet);
    }

    // Supprimer un trajet
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrajet(@PathVariable Integer id) {
        boolean deleted = trajetService.deleteTrajet(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
