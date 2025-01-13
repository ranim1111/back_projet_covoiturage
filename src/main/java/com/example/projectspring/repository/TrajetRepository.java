package com.example.projectspring.repository;

import com.example.projectspring.entity.StatusTrajet;
import com.example.projectspring.entity.Trajet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TrajetRepository extends JpaRepository<Trajet, Long> {
    Optional<Trajet> findById(Long id);

    List<Trajet> findByConducteurId(Integer conducteurId);

    List<Trajet> findByPassagersId(Integer passagerId);

    List<Trajet> findByStatut(StatusTrajet statut);

    long countByStatut(StatusTrajet statut);

    List<Trajet> findByHoraireDepart(String horaireDepart);
}
