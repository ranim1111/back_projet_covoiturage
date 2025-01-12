package com.example.projectspring.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "trajet_id", nullable = false)
    private Trajet trajet;

    @ManyToOne
    @JoinColumn(name = "passager_id", nullable = false)
    private Users passager;

    @Column(nullable = false)
    private LocalDateTime dateReservation;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusReservation status;

    @Column
    private String annulationJustification;

    // Constructeurs
    public Reservation() {
    }

    public Reservation(Trajet trajet, Users passager, LocalDateTime dateReservation, StatusReservation status) {
        this.trajet = trajet;
        this.passager = passager;
        this.dateReservation = dateReservation;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Trajet getTrajet() {
        return trajet;
    }

    public void setTrajet(Trajet trajet) {
        this.trajet = trajet;
    }

    public Users getPassager() {
        return passager;
    }

    public void setPassager(Users passager) {
        this.passager = passager;
    }

    public LocalDateTime getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(LocalDateTime dateReservation) {
        this.dateReservation = dateReservation;
    }

    public StatusReservation getStatus() {
        return status;
    }

    public void setStatus(StatusReservation status) {
        this.status = status;
    }

    public String getAnnulationJustification() {
        return annulationJustification;
    }

    public void setAnnulationJustification(String annulationJustification) {
        this.annulationJustification = annulationJustification;
    }
}
