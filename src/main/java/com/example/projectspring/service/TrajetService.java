package com.example.projectspring.service;

import com.example.projectspring.entity.Trajet;
import com.example.projectspring.repository.TrajetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrajetService {

    @Autowired
    private TrajetRepository trajetRepository;

    // Ajouter un trajet
    public Trajet ajouterTrajet(Trajet trajet) {
        return trajetRepository.save(trajet);
    }

    // Récupérer tous les trajets
    public List<Trajet> getAllTrajets() {
        return trajetRepository.findAll();
    }

    // Récupérer un trajet par son ID
    public Optional<Trajet> getTrajetById(Integer id) {
        return trajetRepository.findById(Long.valueOf(id));
    }

    // Récupérer les trajets d'un conducteur
    public List<Trajet> getTrajetsByConducteur(Integer conducteurId) {
        return trajetRepository.findByConducteurId(conducteurId);
    }

    // Récupérer les trajets d'un passager
    public List<Trajet> getTrajetsByPassager(Integer passagerId) {
        return trajetRepository.findByPassagersId(passagerId);
    }

    // Mettre à jour un trajet
    public Optional<Trajet> updateTrajet(Integer id, Trajet trajet) {
        Optional<Trajet> existingTrajet = trajetRepository.findById(Long.valueOf(id));

        if (existingTrajet.isPresent()) {
            Trajet updatedTrajet = existingTrajet.get();

            // Updating only the non-null fields
            if (trajet.getDepart() != null) updatedTrajet.setDepart(trajet.getDepart());
            if (trajet.getArrivee() != null) updatedTrajet.setArrivee(trajet.getArrivee());
            if (trajet.getHoraireDepart() != null) updatedTrajet.setHoraireDepart(trajet.getHoraireDepart());
            if (trajet.getHoraireArrivee() != null) updatedTrajet.setHoraireArrivee(trajet.getHoraireArrivee());
            if (trajet.getPassagers() != null) updatedTrajet.setPassagers(trajet.getPassagers());
            if (trajet.getDateCreation() != null) updatedTrajet.setDateCreation(trajet.getDateCreation());
            if (trajet.getStatut() != null) updatedTrajet.setStatut(trajet.getStatut());
            if (trajet.getDistance() != null) updatedTrajet.setDistance(trajet.getDistance());
            if (trajet.getTarif() != null) updatedTrajet.setTarif(trajet.getTarif());
            if (trajet.getDescription() != null) updatedTrajet.setDescription(trajet.getDescription());
            if (trajet.getHoraireArriveeEstimee() != null) updatedTrajet.setHoraireArriveeEstimee(trajet.getHoraireArriveeEstimee());

            try {
                // Save the updated trajet and return it
                return Optional.of(trajetRepository.save(updatedTrajet));
            } catch (Exception e) {
                // Log the exception if save fails
                System.err.println("Error while saving updated trajet: " + e.getMessage());
                e.printStackTrace();
                return Optional.empty();
            }
        } else {
            System.out.println("Trajet not found with ID: " + id);
            return Optional.empty(); // Trajet not found
        }
    }


    // Supprimer un trajet
    public boolean deleteTrajet(Integer id) {
        if (trajetRepository.existsById(Long.valueOf(id))) {
            trajetRepository.deleteById(Long.valueOf(id));
            return true; // Suppression réussie
        }
        return false; // Trajet non trouvé pour suppression
    }
}