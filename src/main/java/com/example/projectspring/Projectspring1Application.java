package com.example.projectspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories(basePackages = "com.example.projectspring.repository")
public class Projectspring1Application {

	public static void main(String[] args) {
		SpringApplication.run(Projectspring1Application.class, args);
	}
	

}
