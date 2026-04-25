package com.project.elephant.controllers;

import com.project.elephant.dto.request.LoginRequest;
import com.project.elephant.dto.request.SignupRequest;
import com.project.elephant.dto.response.MessageResponse;
import com.project.elephant.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
        try {
            return ResponseEntity.ok(authService.registerUser(signUpRequest));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            return ResponseEntity.ok(authService.authenticateUser(loginRequest));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PostMapping("/google")
    public ResponseEntity<?> authenticateGoogleUser(@RequestBody Map<String, String> requestBody) {
        try {
            String idTokenString = requestBody.get("token");
            if (idTokenString == null || idTokenString.isEmpty()) {
                return ResponseEntity.badRequest().body(new MessageResponse("Google token is required"));
            }
            return ResponseEntity.ok(authService.authenticateGoogleUser(idTokenString));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}