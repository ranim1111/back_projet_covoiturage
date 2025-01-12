package com.example.projectspring.repository;

import com.example.projectspring.entity.StatusTrajet;
import com.example.projectspring.entity.Trajet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TrajetRepository extends JpaRepository<Trajet, Integer> {

    List<Trajet> findByConducteurId(Integer conducteurId);
    List<Trajet> findByPassagersId(Integer passagerId);

    // Utilisez le nom de la propriété correcte 'dateDepart'
    long countByDateDepartAfter(LocalDateTime dateDepart);


    List<Trajet> findByStatut(StatusTrajet statut);
    long countByStatut(StatusTrajet statut);
    List<Trajet> findByDateDepart(LocalDate dateDepart);

}


