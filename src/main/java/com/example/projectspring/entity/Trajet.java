package com.example.projectspring.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "trajets")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Trajet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-generation for primary key
    private Long id;

    // Départ du trajet
    @Column(nullable = false)
    private String depart;

    // Arrivée du trajet
    @Column(nullable = false)
    private String arrivee;

    // Horaire de départ
    @Column(nullable = false)
    private String horaireDepart;

    // Horaire d'arrivée
    @Column(nullable = false)
    private String horaireArrivee;

    // Conducteur du trajet
    @ManyToOne
    @JoinColumn(name = "conducteur_id", nullable = false)
    private Users conducteur; // Conducteur du trajet

    // Liste des passagers
    @ManyToMany
    @JoinTable(
            name = "trajet_passagers",
            joinColumns = @JoinColumn(name = "trajet_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<Users> passagers; // Liste des passagers

    // Date de création du trajet (Utiliser la méthode @PrePersist pour automatiser la génération de la date)
    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date dateCreation;

    @PrePersist
    public void prePersist() {
        // Assignation de la date de création avant la persistance
        this.dateCreation = new java.util.Date();
    }

    // Statut du trajet (actif, terminé, etc.)
    @Column(nullable = false)
    private String statut;

    // Durée du trajet (en minutes ou autres unités)
    @Column(nullable = false)
    private Integer duree;

    // Distance du trajet (en kilomètres)
    @Column(nullable = false)
    private Double distance;

    // Tarif du trajet
    @Column(nullable = false)
    private Double tarif;

    // Informations supplémentaires sur le trajet (facultatif)
    @Column(length = 500)
    private String description;

    // Horaire estimé d'arrivée
    @Column(nullable = false)
    private String horaireArriveeEstimee;

    // Méthode pour obtenir des détails sur le trajet
    public String getDetailsTrajet() {
        return "Trajet de " + depart + " à " + arrivee + " avec un tarif de " + tarif + "€.";
    }

    // Méthode pour calculer la durée estimée en minutes, ou une autre logique personnalisée
    public Integer calculerDureeEstimee() {
        if (distance != null && distance > 0) {
            // Exemple : vitesse moyenne de 60 km/h
            int vitesseMoyenne = 60;
            return (int) (distance / vitesseMoyenne * 60); // Durée estimée en minutes
        }
        return duree != null ? duree : 0;
    }
}
