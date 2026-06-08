package com.project.elephant.config;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class KeepAliveScheduler {

    // Runs every 12 minutes (720,000 milliseconds)
    @Scheduled(fixedRate = 720000)
    public void keepAlive() {
        System.out.println("[KeepAlive] Backend is active - " + java.time.LocalDateTime.now());
    }
}