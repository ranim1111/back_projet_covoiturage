package com.example.projectspring.service;


import com.example.projectspring.entity.StatusReservation;
import com.example.projectspring.repository.UserRepository;
import com.example.projectspring.repository.TrajetRepository;
import com.example.projectspring.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import com.example.projectspring.entity.StatusReservation;
import com.example.projectspring.entity.StatusTrajet;
import org.springframework.web.bind.annotation.GetMapping;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class AdminService {


    private final UserRepository usersRepository;
    private final TrajetRepository trajetRepository;
    private final ReservationRepository reservationRepository;


    public AdminService(UserRepository usersRepository, TrajetRepository trajetRepository, ReservationRepository reservationRepository) {
        this.usersRepository = usersRepository;
        this.trajetRepository = trajetRepository;
        this.reservationRepository = reservationRepository;
    }


    public long getTotalUsers() {
        return usersRepository.count();
    }


    public long getTotalTrajets() {
        return trajetRepository.count();
    }


    public long getTotalReservations() {
        return reservationRepository.count();
    }


    public long getConfirmedReservations() {
        return reservationRepository.countByStatus(StatusReservation.CONFIRMED);
    }


    public long getCancelledTrajets() {
        return trajetRepository.countByStatut(StatusTrajet.CANCELLED); // Utilisation de "statut"
    }


    public long getTrajetsInProgress() {
        return trajetRepository.countByStatut(StatusTrajet.ACTIF); // Utilisation de "statut"
    }


    //public Long getFutureTrajets(LocalDate date) {
        //return trajetRepository.countTrajetsByDateDepartAfter(date);
    //}

    public long getCancelledReservations() {
        return reservationRepository.countByStatus(StatusReservation.REFUSED);
    }


    public long getPendingReservations() {
        return reservationRepository.countByStatus(StatusReservation.PENDING);
    }


    public long getUsersWithInappropriateBehavior() {
        return usersRepository.countByBehaviorReported(true);
    }






    public double getAverageReservationsPerTrajet() {
        return (double) reservationRepository.count() / trajetRepository.count();
    }


    public double getAverageReservationsPerPassager() {
        return (double) reservationRepository.count() / usersRepository.count();
    }




    //public Map<String, Long> getUsersByVille() {
    //Map<String, Long> regionStats = new HashMap<>();
    //List<String> villes = usersRepository.findAllVilles(); // Implémenter une méthode dans le repository
    //for (String ville : villes) {
    //regionStats.put(ville, usersRepository.countByVille(ville));
    //}
    //return regionStats;
    //}
}
