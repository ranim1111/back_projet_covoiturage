package com.example.projectspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;

import java.util.Optional;


import com.example.projectspring.entity.Users;



public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByUsername(String username);
    Users findByEmail(String email);
    Users findByPhoneNumber(String phoneNumber);
    Optional<Users> findById(Long id);


}