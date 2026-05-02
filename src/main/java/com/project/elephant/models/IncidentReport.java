package com.project.elephant.models;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Document(collection = "incident_reports")
public class IncidentReport {
    @Id
    private String id;
    private String incidentType;
    private String location;
    private String description;
    private String photoUrl;
    private String name;
    private String contactNumber;
    private String username;
    private String status = "Pending";

    @CreatedDate
    private LocalDateTime createdAt;
}
