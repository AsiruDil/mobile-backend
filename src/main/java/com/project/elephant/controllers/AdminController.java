package com.project.elephant.controllers;

import com.project.elephant.dto.response.MessageResponse;
import com.project.elephant.models.User;
import com.project.elephant.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    // 1. Block or Unblock a User
    @PutMapping("/users/{username}/block")
    public MessageResponse toggleBlockUser(@PathVariable String username, @RequestParam boolean status) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setBlocked(status);
        userRepository.save(user);

        String action = status ? "blocked" : "unblocked";
        return new MessageResponse("User " + username + " has been " + action);
    }

    // 2. Change User Role (FULLY SECURED WITH JWT CHECK)
    @PutMapping("/users/{username}/role")
    public MessageResponse changeUserRole(@PathVariable String username, @RequestParam String newRole) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Clean up the input (e.g., if you pass "admin", it safely becomes "ROLE_ADMIN")
        String formattedRole = newRole.toUpperCase();
        if (!formattedRole.startsWith("ROLE_")) {
            formattedRole = "ROLE_" + formattedRole;
        }

        // --- THE JWT SECURITY CHECK ---
        // If someone is trying to grant the powerful "ROLE_ADMIN" status...
        if (formattedRole.equals("ROLE_ADMIN")) {

            // 1. Extract the current logged-in user from the JWT Security Context
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // 2. Check their token authorities to see if THEY are actually an Admin
            boolean isCurrentUserAdmin = authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

            // 3. Block them if they are not
            if (!isCurrentUserAdmin) {
                throw new RuntimeException("Security Violation: Only existing Admins can create new Admins.");
            }
        }

        user.setRole(formattedRole);
        userRepository.save(user);
        return new MessageResponse("User role successfully updated to " + formattedRole);
    }


// Inside AdminController.java:

    // 3. Get all users (Admin only)
    @GetMapping("/users")
    public List<User> getAllUsers() {
        // userRepository.findAll() is a built-in Spring Data MongoDB method
        return userRepository.findAll();
    }
}