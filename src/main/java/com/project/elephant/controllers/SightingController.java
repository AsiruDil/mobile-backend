package com.project.elephant.controllers;

import com.project.elephant.dto.response.MessageResponse;
import com.project.elephant.models.Sighting;
import com.project.elephant.repository.SightingRepository;
import com.project.elephant.services.ExpoPushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sightings")
public class SightingController {

    @Autowired
    private SightingRepository sightingRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ExpoPushService expoPushService;

    @GetMapping
    public ResponseEntity<List<Sighting>> getAllSightings() {
        return ResponseEntity.ok(sightingRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Sighting> reportSighting(@RequestBody Sighting sighting) {
        Sighting savedSighting = sightingRepository.save(sighting);
        return ResponseEntity.ok(savedSighting);
    }

    // අලුතින් යාවත්කාලීන කළ සහ ආරක්ෂිත Alert Endpoint එක
    @PostMapping("/alert")
    public ResponseEntity<MessageResponse> triggerCameraAlert(@RequestBody Map<String, Object> payload) {
        try {
            // "message" එක ආවේ නැත්නම් "Unknown Alert" කියලා ගන්නවා
            String message = (String) payload.getOrDefault("message", "Unknown Alert");
            System.out.println("🚨 ALERT FROM CAMERA RECEIVED IN BACKEND: " + message);

            // 1. WebSocket Broadcast (Crash නොවී යැවීම)
            try {
                messagingTemplate.convertAndSend("/topic/alerts", (Object) payload);
                System.out.println("✅ WebSocket alert broadcasted successfully");
            } catch (Exception e) {
                System.out.println("⚠️ WebSocket broadcast failed: " + e.getMessage());
            }

            // 2. Expo Push Notifications (Crash නොවී යැවීම)
            try {
                expoPushService.sendAlertToEveryone(message);
                System.out.println("✅ Expo push notifications sent successfully");
            } catch (Exception e) {
                System.out.println("⚠️ Expo Push Service failed: " + e.getMessage());
            }

            // කිසිම Crash වීමක් නැතුව අනිවාර්යයෙන්ම Python එකට 200 OK යවනවා
            return ResponseEntity.ok(new MessageResponse("Alert broadcasted successfully"));

        } catch (Exception e) {
            // මොකක් හරි ලොකු Error එකක් ආවොත් සර්වර් එක Crash වෙන්න නොදී 500 Error එකක් යවනවා
            System.out.println("❌ Fatal Error in triggerCameraAlert: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(new MessageResponse("Internal Backend Error"));
        }
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<MessageResponse> deleteSighting(@PathVariable String id) {
        sightingRepository.deleteById(id);
        return ResponseEntity.ok(new MessageResponse("Sighting deleted successfully"));
    }
}