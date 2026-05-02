package com.project.elephant.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "point_locations")
public class PointLocation {
    @Id
    private String id;
    private double latitude;
    private double longitude;
    private String name;
    private String type; // e.g., "Safe Zone", "Water Source", "Ranger Post"
}