package com.example.projectspring.repository;

import com.example.projectspring.entity.Reservation;
import com.example.projectspring.entity.StatusReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByTrajet_Id(Long trajetId);
    List<Reservation> findByPassager_Id(Long passagerId);
    long countByStatus(StatusReservation status);
    boolean existsByTrajet_IdAndPassager_Id(Long trajetId, Long passagerId);
}
