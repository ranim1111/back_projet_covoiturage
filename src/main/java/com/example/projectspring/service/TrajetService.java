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
        return trajetRepository.findById(id);
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
        Optional<Trajet> existingTrajet = trajetRepository.findById(id);

        if (existingTrajet.isPresent()) {
            Trajet updatedTrajet = existingTrajet.get();
            // Mise à jour des informations de trajet
            updatedTrajet.setDepart(trajet.getDepart());
            updatedTrajet.setArrivee(trajet.getArrivee());
            updatedTrajet.setHoraireDepart(trajet.getHoraireDepart());
            updatedTrajet.setHoraireArrivee(trajet.getHoraireArrivee());
            updatedTrajet.setConducteur(trajet.getConducteur());
            updatedTrajet.setPassagers(trajet.getPassagers()); // Correction du nom de méthode pour la liste des passagers
            updatedTrajet.setDateCreation(trajet.getDateCreation()); // Si vous voulez mettre à jour la date de création (attention, elle pourrait être générée automatiquement)
            updatedTrajet.setStatut(trajet.getStatut());
            updatedTrajet.setDuree(trajet.getDuree());
            updatedTrajet.setDistance(trajet.getDistance());
            updatedTrajet.setTarif(trajet.getTarif());
            updatedTrajet.setDescription(trajet.getDescription());
            updatedTrajet.setHoraireArriveeEstimee(trajet.getHoraireArriveeEstimee());

            // Enregistrer et retourner le trajet mis à jour
            return Optional.of(trajetRepository.save(updatedTrajet));
        } else {
            return Optional.empty(); // Si le trajet n'existe pas
        }
    }

    // Supprimer un trajet
    public boolean deleteTrajet(Integer id) {
        if (trajetRepository.existsById(id)) {
            trajetRepository.deleteById(id);
            return true; // Suppression réussie
        }
        return false; // Trajet non trouvé pour suppression
    }
}
