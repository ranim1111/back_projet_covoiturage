package com.example.projectspring.repository;

import com.example.projectspring.entity.Reservation;
import com.example.projectspring.entity.StatusReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    List<Reservation> findByTrajet_Id(Integer trajetId);
    List<Reservation> findByPassager_Id(Integer passagerId);
    long countByStatus(StatusReservation status);

}
