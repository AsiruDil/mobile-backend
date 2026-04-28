package com.project.elephant.services;

import com.project.elephant.models.User;
import com.project.elephant.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExpoPushService {

    @Autowired
    private UserRepository userRepository;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String EXPO_PUSH_URL = "https://exp.host/--/api/v2/push/send";

    public void sendAlertToEveryone(String message) {
        List<User> allUsers = userRepository.findAll();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        for (User user : allUsers) {
            // --- NEW: Skip users who have turned off push alerts ---
            if (!user.isPushAlertsEnabled()) {
                continue;
            }

            String token = user.getPushToken();

            if (token != null && !token.isEmpty()) {
                Map<String, Object> payload = new HashMap<>();
                payload.put("to", token);
                payload.put("sound", "alert.wav");
                payload.put("channelId", "elephant-alerts");
                payload.put("priority", "high");
                payload.put("title", "⚠️ ELEPHANT DETECTED");
                payload.put("body", message);

                Map<String, String> data = new HashMap<>();
                data.put("type", "ELEPHANT_ALERT");
                payload.put("data", data);

                try {
                    HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);
                    restTemplate.postForObject(EXPO_PUSH_URL, request, String.class);
                } catch (Exception e) {
                    System.err.println("Failed to send to " + user.getUsername() + ": " + e.getMessage());
                }
            }
        }
    }
}