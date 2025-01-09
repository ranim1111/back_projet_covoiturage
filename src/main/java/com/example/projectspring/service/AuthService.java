package com.example.projectspring.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.projectspring.configuration.JwtUtils;

import com.example.projectspring.dto.RegisterRequest;
import com.example.projectspring.dto.RegisterResponse;
import com.example.projectspring.entity.Roles;
import com.example.projectspring.entity.Users;
import com.example.projectspring.exception.UserNotFoundException;
import com.example.projectspring.repository.RolesRepository;
import com.example.projectspring.repository.UserRepository;
import com.example.projectspring.response.ApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    
    @Autowired
    private AuthenticationManager authenticationManager; // Injected here
    
    public AuthService(UserRepository userRepository, RolesRepository rolesRepository,
                       PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.rolesRepository = rolesRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    public RegisterResponse registerUser(RegisterRequest registerRequest) {
        // Check for existing username, email, or phone number
        if (userRepository.findByUsername(registerRequest.getUsername()) != null) {
            return new RegisterResponse(false, "Username is already in use", null);
        }

        if (userRepository.findByEmail(registerRequest.getEmail()) != null) {
            return new RegisterResponse(false, "Email is already in use", null);
        }

        if (userRepository.findByPhoneNumber(registerRequest.getPhoneNumber()) != null) {
            return new RegisterResponse(false, "Phone number is already in use", null);
        }

        // Check if the role exists
        Roles role = rolesRepository.findRoleByName(registerRequest.getRole());
        if (role == null) {
            return new RegisterResponse(false, "Role not found", null);
        }

        // Create a new user entity
        Users newUser = new Users();
        newUser.setFirstName(registerRequest.getFirstName());
        newUser.setLastName(registerRequest.getLastName());
        newUser.setPhoneNumber(registerRequest.getPhoneNumber());
        newUser.setEmail(registerRequest.getEmail());
        newUser.setUsername(registerRequest.getUsername());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newUser.setVille(registerRequest.getVille());
        newUser.setNaturePersonne(registerRequest.getNaturePersonne());
        newUser.setRole(role);

        // Save the user
        Users savedUser = userRepository.save(newUser);
        return new RegisterResponse(true, "User registered successfully", savedUser);
    }

    public Map<String, Object> authenticateUser(Users user) throws AuthenticationException {
        try {
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            
            if (authentication.isAuthenticated()) {
                // Fetch the user from DB
                Users userFromDb = userRepository.findByUsername(user.getUsername());

                // Generate JWT token
                String token = jwtUtils.generateToken(user.getUsername());

                // Get the expiration date of the token
                Date expirationDate = jwtUtils.extractExpirationDate(token);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedExpirationDate = dateFormat.format(expirationDate);

                // Prepare response data
                Map<String, Object> authData = new HashMap<>();
                authData.put("token", token);
                authData.put("type", "Bearer");
                authData.put("role", userFromDb.getRole()); 
                authData.put("username", userFromDb.getUsername()); 
                authData.put("email", userFromDb.getEmail()); 
                authData.put("phonenumber", userFromDb.getPhoneNumber()); 
                authData.put("id", userFromDb.getId()); 
                authData.put("statut", userFromDb.getNaturePersonne()); 
                authData.put("expiration", formattedExpirationDate);

                return authData;
            }
            throw new AuthenticationException("Invalid username or password") {};
        } catch (AuthenticationException e) {
            throw e;  // Rethrow exception to be handled in controller
        } catch (Exception e) {
            throw new AuthenticationException("Internal server error") {};
        }
    }
    public Map<String, Object> getUserProfile(String token) {
        try {
            // Extract the username from the token
            String username = jwtUtils.extractUsername(token);
            
         // Check if the username is valid
            if (username == null || username.isEmpty()) {
                throw new UserNotFoundException("User not found or token is invalid");
            }

            // Fetch the user details from the database
            Users userFromDb = userRepository.findByUsername(username);

            if (userFromDb == null) {
                throw new UserNotFoundException("User not found");
            }

            // Prepare response data with user details
            Map<String, Object> userProfileData = new HashMap<>();
            userProfileData.put("username", userFromDb.getUsername());
            userProfileData.put("email", userFromDb.getEmail());
            userProfileData.put("phonenumber", userFromDb.getPhoneNumber());
            userProfileData.put("role", userFromDb.getRole());
            userProfileData.put("ville", userFromDb.getVille());
            userProfileData.put("firstName", userFromDb.getFirstName());
            userProfileData.put("lastName", userFromDb.getLastName());
            userProfileData.put("naturePersonne", userFromDb.getNaturePersonne());
            userProfileData.put("id", userFromDb.getId());

            return userProfileData;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching user profile", e);
        }
    }
 // Update user profile method
    public ApiResponse updateProfile(Map<String, Object> updatedData,String token) {
        try {
        	 // Extract the username from the token
            String username = jwtUtils.extractUsername(token);
            
         // Check if the username is valid
            if (username == null || username.isEmpty()) {
                throw new UserNotFoundException("User not found or token is invalid");
            }

            // Find the existing user
            Users user = userRepository.findByUsername(username);
            if (user == null) {
                return new ApiResponse(false, "User not found", null, null);
            }

            // Update user fields if provided
            if (updatedData.containsKey("firstName")) user.setFirstName((String) updatedData.get("firstName"));
            if (updatedData.containsKey("lastName")) user.setLastName((String) updatedData.get("lastName"));
            if (updatedData.containsKey("username")) user.setUsername((String) updatedData.get("username"));
            if (updatedData.containsKey("email")) user.setEmail((String) updatedData.get("email"));
            if (updatedData.containsKey("phoneNumber")) user.setPhoneNumber((String) updatedData.get("phoneNumber"));
            if (updatedData.containsKey("ville")) user.setVille((String) updatedData.get("ville"));
            if (updatedData.containsKey("naturePersonne")) user.setNaturePersonne((String) updatedData.get("naturePersonne"));

            // Update the role if provided
            if (updatedData.containsKey("role")) {
                String roleName = (String) updatedData.get("role");
                List<String> validRoles = List.of("ADMINISTRATEUR", "CONDUCTEUR", "PASSAGER");

                if (!validRoles.contains(roleName.toUpperCase())) {
                    return new ApiResponse(false, "Invalid role name: " + roleName, null, null);
                }

                // Retrieve the new role
                Roles newRole = rolesRepository.findRoleByName(roleName.toUpperCase());
                if (newRole == null) {
                    return new ApiResponse(false, "Role not found: " + roleName, null, null);
                }
                user.setRole(newRole);
            }

            // Save the updated user
            Users savedUser = userRepository.save(user);

            // Return success response with the updated user
            return new ApiResponse(true, "User profile updated successfully", null, savedUser);

        } catch (Exception e) {
            return new ApiResponse(false, "An error occurred while updating the profile", null, null);
        }
    }
    public ApiResponse deleteProfile(String token) {
        try {
        	 // Extract the username from the token
            String username = jwtUtils.extractUsername(token);
            
         // Check if the username is valid
            if (username == null || username.isEmpty()) {
                throw new UserNotFoundException("User not found or token is invalid");
            }
            // Find the user by username
            Users user = userRepository.findByUsername(username);
            if (user == null) {
                return new ApiResponse(false, "User not found", null, null);
            }

            // Delete the user
            userRepository.delete(user);

            // Return success response
            return new ApiResponse(true, "User profile deleted successfully", null, null);
        } catch (Exception e) {
            return new ApiResponse(false, "An error occurred while deleting the profile", null, null);
        }
    }
    public Users getUserFromToken(String token) {
        // Extract the username from the token
        String username = jwtUtils.extractUsername(token);

        // Check if the username is valid
        if (username == null || username.isEmpty()) {
            throw new UserNotFoundException("User not found or token is invalid");
        }

        // Fetch the user from the database
        Users userFromDb = userRepository.findByUsername(username);

        // If the user is not found, throw an exception
        if (userFromDb == null) {
            throw new UserNotFoundException("User not found");
        }

        // Return the user
        return userFromDb;
    }


}
