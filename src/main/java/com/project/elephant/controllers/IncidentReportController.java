package com.project.elephant.controllers;

import com.project.elephant.dto.request.IncidentReportDTO;
import com.project.elephant.dto.response.MessageResponse;
import com.project.elephant.models.IncidentReport;
import com.project.elephant.services.IncidentReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/incidents" )
public class IncidentReportController {

    @Autowired
    private IncidentReportService incidentReportService;

    @PostMapping("/report")
    public ResponseEntity<?> createReport(@RequestBody IncidentReportDTO dto) {
        try {
            IncidentReport saved = incidentReportService.saveIncidentReport(dto);
            return ResponseEntity.ok(new MessageResponse("Incident reported! ID: " + saved.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }
}

