package com.project.elephant.controllers;

import com.project.elephant.dto.response.MessageResponse;
import com.project.elephant.models.User;
import com.project.elephant.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // Normal users use this route to update their own image
    // Usage: PUT http://localhost:8080/api/users/asiru/image?url=https://supabase.com/myimage.jpg
    @PutMapping("/{username}/image")
    public MessageResponse updateMyProfileImage(@PathVariable String username, @RequestParam String url) {

        // 1. Extract the username from the JWT token of the person making the request
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();

        // 2. SECURITY CHECK: Are they trying to change someone else's profile?
        if (!loggedInUsername.equals(username)) {
            throw new RuntimeException("Security Violation: You can only update your own profile!");
        }

        // 3. If the names match, allow the update!
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setImageUrl(url);
        userRepository.save(user);

        return new MessageResponse("Your profile image was updated successfully.");
    }

    @PutMapping("/{username}/token")
    public MessageResponse updatePushToken(@PathVariable String username, @RequestBody Map<String, String> request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPushToken(request.get("token"));
        userRepository.save(user);

        return new MessageResponse("Push token updated successfully.");
    }
}