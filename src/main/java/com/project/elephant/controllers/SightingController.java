package com.project.elephant.controllers;

import com.project.elephant.dto.response.MessageResponse;
import com.project.elephant.models.Sighting;
import com.project.elephant.repository.SightingRepository;
import com.project.elephant.services.ExpoPushService; // <-- 1. මේක අලුතින් Import කරන්න
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

    @PostMapping("/alert")
    public ResponseEntity<MessageResponse> triggerCameraAlert(@RequestBody Map<String, Object> payload) {
        String message = (String) payload.get("message");
        System.out.println("🚨 ALERT FROM CAMERA: " + message);


        messagingTemplate.convertAndSend("/topic/alerts", (Object) payload);


        expoPushService.sendAlertToEveryone(message);

        return ResponseEntity.ok(new MessageResponse("Alert broadcasted successfully"));
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<MessageResponse> deleteSighting(@PathVariable String id) {
        sightingRepository.deleteById(id);
        return ResponseEntity.ok(new MessageResponse("Sighting deleted successfully"));
    }
}