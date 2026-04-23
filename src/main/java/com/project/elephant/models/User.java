package com.project.elephant.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "users")
public class User {
    @Id
    private String username;
    private String email;

    @JsonIgnore
    private String password;
    private String role;

    private boolean isBlocked;
    private boolean isRegistered;
    private String imageUrl;
}
