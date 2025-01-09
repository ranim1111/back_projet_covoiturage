package com.example.projectspring.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.example.projectspring.entity.Roles;


public interface RolesRepository extends JpaRepository<Roles, Integer> {
	Roles findRoleByName(String name);
}

