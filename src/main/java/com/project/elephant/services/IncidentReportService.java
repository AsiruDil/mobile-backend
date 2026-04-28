package com.project.elephant.services;

import com.project.elephant.dto.request.IncidentReportDTO;
import com.project.elephant.models.IncidentReport;
import com.project.elephant.repository.IncidentReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

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

        report.setUsername(currentUsername);
        report.setCreatedAt(LocalDateTime.now());

        return incidentReportRepository.save(report);
    }
}
