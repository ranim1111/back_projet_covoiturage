package com.example.projectspring.controller;

import com.example.projectspring.entity.Reservation;
import com.example.projectspring.entity.StatusReservation;
import com.example.projectspring.service.EmailService;
import com.example.projectspring.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;
    @Autowired
    private EmailService emailService;
    @PostMapping("/{trajetId}/passager/{passagerId}")
    public ResponseEntity<?> createReservation(
            @PathVariable Long trajetId,
            @PathVariable Long passagerId) {
        try {
            // Attempt to create a reservation
            Reservation reservation = reservationService.createReservation(trajetId, passagerId);
            return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
        } catch (Exception e) {
            // Return an error response if something goes wrong
            ErrorResponse errorResponse = new ErrorResponse("An unexpected error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/trajet/{trajetId}")
    public ResponseEntity<List<Reservation>> getReservationsByTrajet(@PathVariable Long trajetId) {
        List<Reservation> reservations = reservationService.getReservationsByTrajet(trajetId);
        if (reservations.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/passager/{passagerId}")
    public ResponseEntity<List<Reservation>> getReservationsByPassager(@PathVariable Long passagerId) {
        List<Reservation> reservations = reservationService.getReservationsByPassager(passagerId);
        if (reservations.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reservations);
    }

    @PutMapping("/{reservationId}/status")
    public ResponseEntity<?> updateReservationStatus(
            @PathVariable Long reservationId,
            @RequestParam StatusReservation status) {
        try {
            // Mise à jour du statut de la réservation via le service
            Reservation updatedReservation = reservationService.updateReservationStatus(reservationId, status);

            // Envoi de l'email de notification au passager (si applicable)
            String subject = "Statut de votre réservation mis à jour";
            String messageBody = "Le statut de votre réservation a été mis à jour. Nouveau statut: " + status;
            emailService.sendReservationStatusEmail(updatedReservation.getPassager().getEmail(), subject, messageBody);

            // Retourner la réservation mise à jour
            return ResponseEntity.ok(updatedReservation);
        } catch (Exception e) {
            // En cas d'erreur, renvoyer un message d'erreur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Une erreur imprévue est survenue : " + e.getMessage()));
        }
    }



    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/cancel/{reservationId}")
    public ResponseEntity<?> cancelReservation(@PathVariable Long reservationId) {
        try {
            boolean isCancelled = reservationService.cancelReservation(reservationId);

            if (!isCancelled) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to cancel reservation. It may already be cancelled or does not exist.");
            }

            return ResponseEntity.ok("Reservation cancelled successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while cancelling the reservation.");
        }
    }

    // ErrorResponse class
    class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
