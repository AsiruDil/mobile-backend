package com.project.elephant.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {

    // මෙහි අගට : ලකුණක් එක් කිරීමෙන්, variable එක නැති වුවහොත් error එකක් එනවා වෙනුවට හිස් අගයක් ගනී.
    @Value("${MONGO_URI:}")
    private String mongoUri;

    @Bean
    public MongoClient mongoClient() {
        // Variable එක හිස් දැයි පරීක්ෂා කර බැලීම
        if (mongoUri == null || mongoUri.isEmpty() || mongoUri.equals("${MONGO_URI}")) {
            throw new RuntimeException("නිවැරදි කිරීම් අවශ්‍යයි: Render settings හි MONGO_URI අගය ඇතුළත් කර නැත!");
        }

        try {
            return MongoClients.create(mongoUri);
        } catch (Exception e) {
            throw new RuntimeException("MongoDB Connection Error: " + e.getMessage());
        }
    }
}