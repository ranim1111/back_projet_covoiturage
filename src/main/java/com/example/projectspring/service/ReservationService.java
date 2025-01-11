package com.example.projectspring.service;

import com.example.projectspring.entity.Reservation;
import com.example.projectspring.entity.Trajet;
import com.example.projectspring.entity.Users;
import com.example.projectspring.entity.StatusReservation;
import com.example.projectspring.repository.ReservationRepository;
import com.example.projectspring.repository.TrajetRepository;
import com.example.projectspring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private TrajetRepository trajetRepository;

    @Autowired
    private UserRepository utilisateurRepository;

    // Corrigez les types des paramètres pour qu'ils soient cohérents avec l'entité Users
    public Reservation createReservation(Long trajetId, Long passagerId) {
        Optional<Trajet> trajet = trajetRepository.findById(trajetId.intValue()); // Convertir Long en Integer
        Optional<Users> passager = utilisateurRepository.findById(passagerId);

        if (trajet.isEmpty() || passager.isEmpty()) {
            throw new IllegalArgumentException("Trajet or Passager not found");
        }

        Reservation reservation = new Reservation();
        reservation.setTrajet(trajet.get());
        reservation.setPassager(passager.get());
        reservation.setDateReservation(LocalDateTime.now());
        reservation.setStatus(StatusReservation.PENDING);

        return reservationRepository.save(reservation);
    }

    public List<Reservation> getReservationsByTrajet(Integer trajetId) {
        return reservationRepository.findByTrajet_Id(trajetId);
    }

    public List<Reservation> getReservationsByPassager(Long passagerId) {
        return reservationRepository.findByPassager_Id(Math.toIntExact(passagerId));
    }

    public Reservation updateReservationStatus(Integer reservationId, StatusReservation status) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
        reservation.setStatus(status);
        return reservationRepository.save(reservation);
    }

    public boolean cancelReservation(Integer reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
        reservationRepository.delete(reservation);
        return true;
    }
}
