package com.example.projectspring.repository;

import com.example.projectspring.entity.Trajet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TrajetRepository extends JpaRepository<Trajet, Integer> {

    List<Trajet> findByConducteurId(Integer conducteurId);

    List<Trajet> findByPassagersId(Integer passagerId);
}
