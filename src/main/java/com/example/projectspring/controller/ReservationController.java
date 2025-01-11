package com.example.projectspring.controller;

import com.example.projectspring.entity.Reservation;
import com.example.projectspring.entity.StatusReservation;
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

    @PostMapping("/{trajetId}/passager/{passagerId}")
    public ResponseEntity<Reservation> createReservation(
            @PathVariable Long trajetId,
            @PathVariable Integer passagerId) {
        Reservation reservation = reservationService.createReservation(trajetId, Long.valueOf(passagerId));
        return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
    }

    @GetMapping("/trajet/{trajetId}")
    public ResponseEntity<List<Reservation>> getReservationsByTrajet(@PathVariable Integer trajetId) {
        List<Reservation> reservations = reservationService.getReservationsByTrajet(trajetId);
        if (reservations.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/passager/{passagerId}")
    public ResponseEntity<List<Reservation>> getReservationsByPassager(@PathVariable Integer passagerId) {
        List<Reservation> reservations = reservationService.getReservationsByPassager(Long.valueOf(passagerId));
        if (reservations.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reservations);
    }

    @PutMapping("/{reservationId}/status")
    public ResponseEntity<Reservation> updateReservationStatus(
            @PathVariable Integer reservationId,
            @RequestParam StatusReservation status) {
        Reservation updatedReservation = reservationService.updateReservationStatus(reservationId, status);
        return ResponseEntity.ok(updatedReservation);
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Integer reservationId) {
        reservationService.cancelReservation(reservationId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/cancel/{reservationId}")
    public ResponseEntity<?> cancelReservation(@PathVariable Long reservationId) {
        try {
            boolean isCancelled = reservationService.cancelReservation(Math.toIntExact(reservationId));

            if (!isCancelled) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to cancel reservation. It may already be cancelled or does not exist.");
            }

            return ResponseEntity.ok("Reservation cancelled successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while cancelling the reservation.");
        }
    }
}
