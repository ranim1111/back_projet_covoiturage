package com.example.projectspring.controller;


import com.example.projectspring.service.AdminService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;


    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }


    @GetMapping("/dashboard-stats")
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        try {
            stats.put("totalUsers", adminService.getTotalUsers());
            stats.put("totalTrajets", adminService.getTotalTrajets());
            stats.put("totalReservations", adminService.getTotalReservations());
            stats.put("confirmedReservations", adminService.getConfirmedReservations());
            stats.put("cancelledTrajets", adminService.getCancelledTrajets());
            stats.put("trajectsInProgress", adminService.getTrajetsInProgress());
            //stats.put("futureTrajets", adminService.getFutureTrajets());
            stats.put("cancelledReservations", adminService.getCancelledReservations());
            stats.put("pendingReservations", adminService.getPendingReservations());
            stats.put("usersWithInappropriateBehavior", adminService.getUsersWithInappropriateBehavior());
            stats.put("averageReservationsPerTrajet", adminService.getAverageReservationsPerTrajet());
            stats.put("averageReservationsPerPassager", adminService.getAverageReservationsPerPassager());
            return stats;
        } catch (Exception e) {
            e.printStackTrace();  // Ou un autre mécanisme de log
            throw new RuntimeException("Erreur dans la récupération des statistiques", e);
        }
    }

}
