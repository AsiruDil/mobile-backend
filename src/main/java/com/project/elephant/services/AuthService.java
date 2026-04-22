package com.project.elephant.services;

import com.project.elephant.dto.request.LoginRequest;
import com.project.elephant.dto.request.SignupRequest;
import com.project.elephant.dto.response.JwtResponse;
import com.project.elephant.dto.response.MessageResponse;
import com.project.elephant.models.User;
import com.project.elephant.repository.UserRepository;
import com.project.elephant.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public MessageResponse registerUser(SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new RuntimeException("Error: Username is already taken!");
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new RuntimeException("Error: Email is already in use!");
        }

        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRole("user");

        userRepository.save(user);

        return new MessageResponse("User registered successfully!");
    }

    // ✅ FIXED: Changed JwtUtil to JwtResponse here
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        String input = loginRequest.getUsernameOrEmail();

        User user = userRepository.findByUsernameOrEmail(input, input)
                .orElseThrow(() -> new RuntimeException("User not found with provided username or email"));

        if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            String token = jwtUtil.generateJwtToken(user.getUsername());
            return new JwtResponse(token, user.getUsername());
        } else {
            throw new RuntimeException("Invalid password");
        }
    }
}