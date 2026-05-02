package com.project.elephant.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {

    // Render හි Environment Variables හරහා MONGO_URI අගය ලබා ගැනීම
    @Value("${MONGO_URI:}")
    private String mongoUri;

    @Bean
    public MongoClient mongoClient() {
        // 1. අගය දෙපැත්තේ ඇති හිස්තැන් (Spaces) ඉවත් කර පිරිසිදු කිරීම
        String cleanUri = (mongoUri != null) ? mongoUri.trim() : "";

        // 2. අගය ලැබී ඇත්දැයි පරීක්ෂා කිරීම
        if (cleanUri.isEmpty() || cleanUri.equals("${MONGO_URI}")) {
            System.err.println("CRITICAL ERROR: MONGO_URI is missing in Render Environment Variables!");
            throw new RuntimeException("Environment variable MONGO_URI is not set correctly.");
        }

        // 3. සබැඳිය නිවැරදි Format එකෙන් ආරම්භ වේදැයි බැලීම
        if (!cleanUri.startsWith("mongodb://") && !cleanUri.startsWith("mongodb+srv://")) {
            System.err.println("CRITICAL ERROR: Invalid MongoDB URI Format! Received: " + cleanUri);
            throw new RuntimeException("The connection string must start with 'mongodb://' or 'mongodb+srv://'");
        }

        try {
            System.out.println("Connecting to MongoDB Atlas...");
            return MongoClients.create(cleanUri);
        } catch (Exception e) {
            System.err.println("MongoDB Connection Error: " + e.getMessage());
            throw new RuntimeException("Failed to create MongoClient: " + e.getMessage());
        }
    }
}