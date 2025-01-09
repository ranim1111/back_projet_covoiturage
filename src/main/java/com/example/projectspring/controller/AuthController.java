package com.example.projectspring.controller;

import lombok.RequiredArgsConstructor;



import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.projectspring.configuration.JwtUtils;
import com.example.projectspring.dto.AuthResponse;
import com.example.projectspring.dto.RegisterRequest;
import com.example.projectspring.dto.RegisterResponse;
import com.example.projectspring.entity.Roles;
import com.example.projectspring.entity.Users;
import com.example.projectspring.exception.UserNotFoundException;
import com.example.projectspring.repository.RolesRepository;
import com.example.projectspring.repository.UserRepository;
import com.example.projectspring.response.ApiResponse;
import com.example.projectspring.service.AuthService;

import jakarta.validation.Valid;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.projectspring.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Register user endpoint
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerUser(@RequestBody RegisterRequest registerRequest) {
        RegisterResponse registerResponse = authService.registerUser(registerRequest);
        return ResponseEntity.status(registerResponse.isSuccess() ? 201 : 400).body(registerResponse);
    }

    // Login (authenticate) user endpoint
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Users user) {
        try {
            // Call the service to authenticate the user
            Map<String, Object> authData = authService.authenticateUser(user);
            return ResponseEntity.ok(authData);  // Return the data in the response
        } catch (AuthenticationException e) {
            // Handle Authentication Exception (wrong credentials or login failed)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        } catch (Exception e) {
            // Handle any other general exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }
    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@RequestHeader("Authorization") String token) {
        try {
            // Remove the "Bearer " prefix from the token
            String jwtToken = token.substring(7);

            // Call the AuthService to get user profile details
            Map<String, Object> userProfile = authService.getUserProfile(jwtToken);
            return ResponseEntity.ok(userProfile);  // Return the user profile

        } catch (UserNotFoundException e) {
            // Handle invalid or missing user profile
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed: " + e.getMessage());
        } catch (Exception e) {
            // Handle other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }
 // Endpoint to update user profile
    @PutMapping("/updateprofile")
    public ResponseEntity<ApiResponse> updateProfile(@RequestBody Map<String, Object> updatedData, @RequestHeader("Authorization") String token) {
    	// Remove the "Bearer " prefix from the token
        String jwtToken = token.substring(7);
    	// Call the service to update the profile
        ApiResponse response = authService.updateProfile(updatedData, jwtToken);
        
        // Return the response from the service
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(response.isSuccess() ? 200 : 400).body(response);
        }
    }
    // delete endpoint
    @DeleteMapping("/deleteprofile")
    public ResponseEntity<ApiResponse> deleteProfile(@RequestHeader("Authorization") String token) {
    	// Remove the "Bearer " prefix from the token
        String jwtToken = token.substring(7);

        // Call the service to delete the profile
        ApiResponse response = authService.deleteProfile(jwtToken);

        // Return the response from the service
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(response.isSuccess() ? 200 : 400).body(response);
        }
    }
}

/*
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    
   
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody Map<String, Object> userMap, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (ObjectError error : result.getAllErrors()) {
                if (error instanceof FieldError) {
                    FieldError fieldError = (FieldError) error;
                    errors.put(fieldError.getField(), fieldError.getDefaultMessage());
                } else {
                    errors.put(error.getObjectName(), error.getDefaultMessage());
                }
            }
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Validation failed", errors, null));
        }
        
        
        Users user = new Users();
        user.setFirstName((String) userMap.get("firstName"));
        user.setLastName((String) userMap.get("lastName"));
        user.setPhoneNumber((String) userMap.get("phoneNumber"));
        user.setEmail((String) userMap.get("email"));
        user.setUsername((String) userMap.get("username"));
        user.setPassword((String) userMap.get("password"));
        user.setVille((String) userMap.get("ville"));
        
        String roleName = (String) userMap.get("role");
        Roles role = rolesRepository.findRoleByName(roleName);
        if (role == null) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Role not found", null, null));
        }
        user.setRole(role);

        
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Username is already in use", null, null));
        }
        
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Email is already in use", null, null));
        }
        
        if (userRepository.findByPhoneNumber(user.getPhoneNumber()) != null) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Phone Number is already in use", null, null));
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        Users savedUser = userRepository.save(user);
        return ResponseEntity.ok(new ApiResponse(true, "User registered successfully", null, savedUser));
    }

   
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Users user) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            if (authentication.isAuthenticated()) {
            	
                Users userFromDb = userRepository.findByUsername(user.getUsername());

                String token = jwtUtils.generateToken(user.getUsername());
                Date expirationDate = jwtUtils.extractExpirationDate(token);
                
              
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedExpirationDate = dateFormat.format(expirationDate);

                
                Map<String, Object> authData = new HashMap<>();
                authData.put("token", jwtUtils.generateToken(user.getUsername()));
                authData.put("type", "Bearer");
                authData.put("role", userFromDb.getRole()); 
                authData.put("username", userFromDb.getUsername()); 
                authData.put("email", userFromDb.getEmail()); 
                authData.put("phonenumber", userFromDb.getPhoneNumber()); 
                authData.put("id", userFromDb.getId()); 
                authData.put("phonenumber", userFromDb.getPhoneNumber()); 
                authData.put("expiration", formattedExpirationDate);
                
                return ResponseEntity.ok(authData);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        } catch (AuthenticationException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
        }
    }
    
   
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        try {
            
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            
            if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "User is not authenticated", null, null));
            }

            
            String username = ((UserDetails) authentication.getPrincipal()).getUsername();

            
            Users user = userRepository.findByUsername(username);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, "User not found", null, null));
            }

            
            return ResponseEntity.ok(new ApiResponse(true, "User profile retrieved successfully", null, user));
        } catch (Exception e) {
            log.error("Error retrieving user profile", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "Error retrieving profile", null, null));
        }
        }
    @PutMapping("/updateprofile")
    public ResponseEntity<?> updateProfile(@RequestBody Map<String, Object> updatedData) {
        try {
            // Get the authenticated user's details
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse(false, "User is not authenticated", null, null));
            }

            // Get the username of the authenticated user
            String username = ((UserDetails) authentication.getPrincipal()).getUsername();

            // Find the existing user
            Users user = userRepository.findByUsername(username);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(false, "User not found", null, null));
            }

            // Update user fields if provided
            if (updatedData.containsKey("firstName")) user.setFirstName((String) updatedData.get("firstName"));
            if (updatedData.containsKey("lastName")) user.setLastName((String) updatedData.get("lastName"));
            if (updatedData.containsKey("username")) user.setUsername((String) updatedData.get("username"));
            if (updatedData.containsKey("email")) user.setEmail((String) updatedData.get("email"));
            if (updatedData.containsKey("phoneNumber")) user.setPhoneNumber((String) updatedData.get("phoneNumber"));
            if (updatedData.containsKey("ville")) user.setVille((String) updatedData.get("ville"));

            // Update the role if provided
            if (updatedData.containsKey("role")) {
                String roleName = (String) updatedData.get("role");
                List<String> validRoles = List.of("ADMINISTRATEUR", "CONDUCTEUR", "PASSAGER");
                
                if (!validRoles.contains(roleName.toUpperCase())) {
                    return ResponseEntity.badRequest()
                            .body(new ApiResponse(false, "Invalid role name: " + roleName, null, null));
                }

                Roles newRole = rolesRepository.findRoleByName(roleName.toUpperCase());
                if (newRole == null) {
                    return ResponseEntity.badRequest()
                            .body(new ApiResponse(false, "Role not found: " + roleName, null, null));
                }
                user.setRole(newRole);
            }

            // Save the updated user
            Users savedUser = userRepository.save(user);

            // Respond with the updated user
            return ResponseEntity.ok(new ApiResponse(true, "User profile updated successfully", null, savedUser));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "An error occurred while updating the profile", null, null));
        }
    }


    @PutMapping("/updateRole")
    public ResponseEntity<ApiResponse> updateRole(@RequestParam String roleName) {
        try {
  
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "User is not authenticated", null, null));
            }

            String username = ((UserDetails) authentication.getPrincipal()).getUsername();

           
            Users user = userRepository.findByUsername(username);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, "User not found", null, null));
            }

            
            Roles newRole = null;
            if ("ADMINISTRATEUR".equalsIgnoreCase(roleName)) {
                newRole = rolesRepository.findRoleByName("ADMINISTRATEUR");
            } else if ("PASSAGER".equalsIgnoreCase(roleName)) {
                newRole = rolesRepository.findRoleByName("PASSAGER");
            } else if ("CONDUCTEUR".equalsIgnoreCase(roleName)) {
                newRole = rolesRepository.findRoleByName("CONDUCTEUR");
            } 
            else {
                return ResponseEntity.badRequest().body(new ApiResponse(false, "Invalid role name", null, null));
            }

           
            if (newRole == null) {
                return ResponseEntity.badRequest().body(new ApiResponse(false, "Role not found", null, null));
            }

            
            user.setRole(newRole);
            Users updatedUser = userRepository.save(user);

            return ResponseEntity.ok(new ApiResponse(true, "User role updated successfully", null, updatedUser));
        } catch (Exception e) {
            log.error("Error updating user role", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "Error updating role", null, null));
        }
    }

   

    
    @DeleteMapping("/deleteaccount")
    public ResponseEntity<?> deleteaccount(){
    	try {
    		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    		            
    		            if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
    		                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "User is not authenticated", null, null));
    		            }

    		            
    		            String username = ((UserDetails) authentication.getPrincipal()).getUsername();

    		            
    		            Users user = userRepository.findByUsername(username);
    		            if (user == null) {
    		                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, "User not found", null, null));
    		            }
    		            
    		            
    		            
    		            userRepository.delete(user);
    		            return ResponseEntity.ok(new ApiResponse(true, "User account was deleted successfully", null, user));

    		        }catch(Exception e){
    		        	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "ERROR while updating your profile please retry later.", null, null));
    		        }
    }
    
    
    //role prédefini
  @PostMapping("/addpassager")
  public ResponseEntity<ApiResponse> addpassager(@Valid @RequestBody Users user, BindingResult result) {
      if (result.hasErrors()) {
          Map<String, String> errors = new HashMap<>();
          for (ObjectError error : result.getAllErrors()) {
              if (error instanceof FieldError) {
                  FieldError fieldError = (FieldError) error;
                  errors.put(fieldError.getField(), fieldError.getDefaultMessage());
              } else {
                  errors.put(error.getObjectName(), error.getDefaultMessage());
              }
          }
          return ResponseEntity.badRequest().body(new ApiResponse(false, "Validation failed", errors, null));
      }
      if (userRepository.findByUsername(user.getUsername()) != null ) {
          return ResponseEntity.badRequest().body(new ApiResponse(false, "Username is already in use", null, null));
      } 
      if (userRepository.findByEmail(user.getEmail()) != null ) {
          return ResponseEntity.badRequest().body(new ApiResponse(false, "Email is already in use", null, null));
      } 
      if (userRepository.findByPhoneNumber(user.getPhoneNumber()) != null ) {
          return ResponseEntity.badRequest().body(new ApiResponse(false, "Phone Number is already in use", null, null));
      } 
   
   // Fetch or create the default role
      Roles defaultRole = rolesRepository.findRoleByName("PASSAGER");
      if (defaultRole == null) {
          return ResponseEntity.badRequest().body(new ApiResponse(false, "Default role not found", null, null));
      }
      user.setRole(defaultRole);

      user.setPassword(passwordEncoder.encode(user.getPassword()));

      Users savedUser = userRepository.save(user);
      return ResponseEntity.ok(new ApiResponse(true, "PASSAGER registered successfully", null, savedUser));
  }
}*/
