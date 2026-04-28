package com.project.elephant.controllers;

import com.project.elephant.dto.request.ProfileUpdateRequest;
import com.project.elephant.dto.response.MessageResponse;
import com.project.elephant.models.User;
import com.project.elephant.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // --- 1. Get logged-in user's full profile ---
    @GetMapping("/me")
    public ResponseEntity<User> getMyProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();

        User user = userRepository.findByUsername(loggedInUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(user);
    }

    // --- 2. Update logged-in user's full profile ---
    @PutMapping("/me")
    public ResponseEntity<MessageResponse> updateMyProfile(@RequestBody ProfileUpdateRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();

        User user = userRepository.findByUsername(loggedInUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update all fields from the new React Native screen
        user.setImageUrl(request.getImageUrl());
        user.setIdCardNumber(request.getIdCardNumber());
        user.setBirthday(request.getBirthday());
        user.setAge(request.getAge());
        user.setGender(request.getGender());
        user.setMobileNumber(request.getMobileNumber());
        user.setPushAlertsEnabled(request.isPushAlertsEnabled());

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Profile updated successfully"));
    }

    // --- 3. Existing: Normal users use this route to update their own image ---
    @PutMapping("/{username}/image")
    public MessageResponse updateMyProfileImage(@PathVariable String username, @RequestParam String url) {

        // Extract the username from the JWT token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();

        // SECURITY CHECK: Are they trying to change someone else's profile?
        if (!loggedInUsername.equals(username)) {
            throw new RuntimeException("Security Violation: You can only update your own profile!");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setImageUrl(url);
        userRepository.save(user);

        return new MessageResponse("Your profile image was updated successfully.");
    }

    // --- 4. Existing: Update push token ---
    @PutMapping("/{username}/token")
    public MessageResponse updatePushToken(@PathVariable String username, @RequestBody Map<String, String> request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPushToken(request.get("token"));
        userRepository.save(user);

        return new MessageResponse("Push token updated successfully.");
    }
}