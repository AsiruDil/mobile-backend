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

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import java.util.Collections;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // 🔴 IMPORTANT: Replace this with your Web Client ID from Google Cloud Console
    private static final String GOOGLE_CLIENT_ID = "YOUR_WEB_CLIENT_ID.apps.googleusercontent.com";

    public MessageResponse registerUser(SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new RuntimeException("Email is already in use!");
        }

        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRole("ROLE_USER");
        user.setBlocked(false);
        user.setRegistered(true);
        user.setImageUrl("");

        userRepository.save(user);

        return new MessageResponse("User registered successfully!");
    }

    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        String input = loginRequest.getUsernameOrEmail();

        User user = userRepository.findByUsernameOrEmail(input, input)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isBlocked()) {
            throw new RuntimeException("Your account has been blocked.");
        }

        if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            String token = jwtUtil.generateJwtToken(user.getUsername());
            return new JwtResponse(token, user.getUsername());
        } else {
            throw new RuntimeException("Invalid password");
        }
    }

    public JwtResponse authenticateGoogleUser(String idTokenString) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singletonList(GOOGLE_CLIENT_ID))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);

            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                String email = payload.getEmail();
                String username = email.split("@")[0];

                User user = userRepository.findByUsernameOrEmail(email, email).orElse(null);

                if (user == null) {
                    user = new User();
                    if(userRepository.existsByUsername(username)) {
                        username = username + System.currentTimeMillis();
                    }
                    user.setUsername(username);
                    user.setEmail(email);
                    user.setPassword(passwordEncoder.encode("GOOGLE_OAUTH_" + System.currentTimeMillis()));
                    user.setRole("ROLE_USER");
                    user.setBlocked(false);
                    user.setRegistered(true);

                    String pictureUrl = (String) payload.get("picture");
                    user.setImageUrl(pictureUrl != null ? pictureUrl : "");

                    userRepository.save(user);
                }

                if (user.isBlocked()) {
                    throw new RuntimeException("Your account has been blocked.");
                }

                String token = jwtUtil.generateJwtToken(user.getUsername());
                return new JwtResponse(token, user.getUsername());

            } else {
                throw new RuntimeException("Invalid Google ID token.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Google Authentication Failed");
        }
    }
}