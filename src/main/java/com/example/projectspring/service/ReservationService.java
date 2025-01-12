package com.example.projectspring.service;

import com.example.projectspring.entity.Reservation;
import com.example.projectspring.entity.Trajet;
import com.example.projectspring.entity.Users;
import com.example.projectspring.entity.StatusReservation;
import com.example.projectspring.exception.NotFoundException;
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

    public Reservation createReservation(Long trajetId, Long passagerId) {
        Optional<Trajet> trajet = trajetRepository.findById(Math.toIntExact(trajetId));
        Optional<Users> passager = utilisateurRepository.findById(passagerId);

        if (trajet.isEmpty() || passager.isEmpty()) {
            throw new IllegalArgumentException("Trajet or Passager not found");
        }

        if (reservationRepository.existsByTrajet_IdAndPassager_Id(trajetId, passagerId)) {
            throw new IllegalStateException("Reservation already exists for this passager and trajet");
        }

        Reservation reservation = new Reservation();
        reservation.setTrajet(trajet.get());
        reservation.setPassager(passager.get());
        reservation.setDateReservation(LocalDateTime.now());
        reservation.setStatus(StatusReservation.PENDING);

        return reservationRepository.save(reservation);
    }

    public List<Reservation> getReservationsByTrajet(Long trajetId) {
        return reservationRepository.findByTrajet_Id(trajetId);
    }

    public List<Reservation> getReservationsByPassager(Long passagerId) {
        return reservationRepository.findByPassager_Id(passagerId);
    }

    public boolean cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
        reservationRepository.delete(reservation);
        return true;
    }

    @Autowired
    private EmailService emailService;

    public Reservation updateReservationStatus(Long reservationId, StatusReservation status) {
        // Recherche de la réservation par ID, ou lance une exception si non trouvée
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        // Mise à jour du statut de la réservation
        reservation.setStatus(status);

        // Sauvegarde et renvoi de la réservation mise à jour
        return reservationRepository.save(reservation);
    }

    private void sendStatusUpdateEmailToPassagers(Reservation reservation) {
        // Récupérer l'email des passagers
        List<Users> passagers = (List<Users>) reservation.getPassager(); // Si vous avez une relation OneToMany, assurez-vous que c'est la bonne méthode

        for (Users passager : passagers) {
            String subject = "Update on your reservation status";
            String messageBody = "Hello " + passager.getFirstName() + ",\n\n" +
                    "The status of your reservation has been updated to: " + reservation.getStatus() + ".\n\n" +
                    "Best regards,\nYour Travel Service";

            // Envoyer l'email
            emailService.sendReservationStatusEmail(passager.getEmail(), subject, messageBody);
        }
    }
}
