package com.project.elephant.controllers;

import com.project.elephant.dto.response.MessageResponse;
import com.project.elephant.models.DangerZone;
import com.project.elephant.repository.DangerZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DangerZoneController {

    @Autowired
    private DangerZoneRepository dangerZoneRepository;

    // Public endpoint for the mobile app to fetch zones
    @GetMapping("/danger-zones")
    public ResponseEntity<List<DangerZone>> getAllDangerZones() {
        return ResponseEntity.ok(dangerZoneRepository.findAll());
    }

    // Admin endpoint to create a zone
    @PostMapping("/admin/danger-zones")
    public ResponseEntity<DangerZone> createDangerZone(@RequestBody DangerZone dangerZone) {
        return ResponseEntity.ok(dangerZoneRepository.save(dangerZone));
    }

    // Admin endpoint to delete a zone
    @DeleteMapping("/admin/danger-zones/{id}")
    public ResponseEntity<MessageResponse> deleteDangerZone(@PathVariable String id) {
        dangerZoneRepository.deleteById(id);
        return ResponseEntity.ok(new MessageResponse("Danger zone deleted"));
    }
}