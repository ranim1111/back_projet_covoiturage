package com.example.projectspring.repository;

import com.example.projectspring.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<Roles, Integer> {
	Roles findRoleByName(String name);
}
