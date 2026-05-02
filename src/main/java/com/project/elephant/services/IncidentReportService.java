package com.project.elephant.services;

import com.project.elephant.dto.request.IncidentReportDTO;
import com.project.elephant.models.IncidentReport;
import com.project.elephant.repository.IncidentReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class IncidentReportService {

    @Autowired
    private IncidentReportRepository incidentReportRepository;

    public IncidentReport saveIncidentReport(IncidentReportDTO dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();

        IncidentReport report = new IncidentReport();
        report.setIncidentType(dto.getIncidentType());
        report.setLocation(dto.getLocation());
        report.setDescription(dto.getDescription());
        report.setPhotoUrl(dto.getPhotoUrl());

        // Mapping the renamed fields
        report.setName(dto.getName());
        report.setContactNumber(dto.getContactNumber());
        report.setStatus("Pending");
        report.setUsername(currentUsername);
        report.setCreatedAt(LocalDateTime.now());

        return incidentReportRepository.save(report);
    }

    public List<IncidentReport> getAllReports() {
        return incidentReportRepository.findAll();
    }

    public IncidentReport updateStatus(String id, String status) {
        IncidentReport report = incidentReportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found"));
        report.setStatus(status);
        return incidentReportRepository.save(report);
    }

    public void deleteReport(String id) {
        incidentReportRepository.deleteById(id);
    }
}
