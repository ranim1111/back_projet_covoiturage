package com.example.projectspring.service;

import com.example.projectspring.entity.StatusReservation;
import com.example.projectspring.repository.UserRepository;
import com.example.projectspring.repository.TrajetRepository;
import com.example.projectspring.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import com.example.projectspring.entity.StatusReservation;

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
}
