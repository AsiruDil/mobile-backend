package com.project.elephant.controllers;

import com.project.elephant.dto.response.MessageResponse;
import com.project.elephant.models.PointLocation;
import com.project.elephant.repository.PointLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PointLocationController {

    @Autowired
    private PointLocationRepository pointLocationRepository;

    // Public endpoint for mobile app
    @GetMapping("/point-locations")
    public ResponseEntity<List<PointLocation>> getAllPoints() {
        return ResponseEntity.ok(pointLocationRepository.findAll());
    }

    // Admin endpoints
    @PostMapping("/admin/point-locations")
    public ResponseEntity<PointLocation> createPoint(@RequestBody PointLocation point) {
        return ResponseEntity.ok(pointLocationRepository.save(point));
    }

    @DeleteMapping("/admin/point-locations/{id}")
    public ResponseEntity<MessageResponse> deletePoint(@PathVariable String id) {
        pointLocationRepository.deleteById(id);
        return ResponseEntity.ok(new MessageResponse("Point location deleted"));
    }
}