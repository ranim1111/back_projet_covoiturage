package com.example.projectspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;

import java.util.Optional;


import com.example.projectspring.entity.Users;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByUsername(String username);
    Users findByEmail(String email);
    Users findByPhoneNumber(String phoneNumber);
    Optional<Users> findById(Long id);
    long countByRoleName(String roleName);
    long countByBehaviorReported(Boolean behaviorReported);
}

