package com.project.elephant.models;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "news")
public class News {
    @Id
    private String id;
    private String title;
    private String description;
    private String imageUrl;
    private boolean enabled;

    @CreatedDate
    private LocalDateTime createdAt;
}