package com.project.elephant.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "danger_zones")
public class DangerZone {
    @Id
    private String id;
    private double latitude;
    private double longitude;
    private double radius; // Radius in meters
    private String name;
}