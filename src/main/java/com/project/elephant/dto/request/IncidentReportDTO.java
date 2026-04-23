package com.project.elephant.dto.request;

import lombok.Data;

@Data
public class IncidentReportDTO {
    private String incidentType;
    private String location;
    private String description;
    private String photoUrl;
    private String name;
    private String contactNumber;
}
