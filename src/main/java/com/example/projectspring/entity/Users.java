package com.example.projectspring.entity;



import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    private Long id;
    @NotBlank(message = "First name is mandatory")
    @Size(min = 3, message = "First name must be more than 3 characters")
    private String firstName;
    
    @NotBlank(message = "First name is mandatory")
    @Size(min = 3, message = "First name must be more than 3 characters")
    private String lastName;
    
    @NotBlank(message = "Email is mandatory")
    @Email(message= "A valid email is required")
    private String email;
    
    @NotBlank(message = "Phone Number is mandatory")
    @Pattern(regexp = "^[0-9]{8}$", message = "Phone number must be an 8 digit number")
    @Column(unique= true)
    private String phoneNumber;
    @NotBlank(message = "Username is mandatory")
    @Size(min = 3, message = "First name must be more than 3 characters")
    private String username;
    @NotBlank(message = "Password is mandatory")
    @Size(min = 5, message = "Password must be more than 5 characters")
    @Pattern(regexp = ".*\\d.*", message = "Password must contain at least one number")
    private String password;
    private String ville;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Roles role;
    @NotBlank(message = "NaturePersonne is mandatory")
    private String NaturePersonne; 
    


}
