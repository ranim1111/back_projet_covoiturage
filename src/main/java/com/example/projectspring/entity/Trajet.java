package com.example.projectspring.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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
    @Size(max = 4, message = "Le nombre maximum de passagers est 4")
    private List<Users> passagers= new ArrayList<>(); 

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
    private String statut= "Actif";


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


}
