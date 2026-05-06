package com.project.elephant.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tunnel")
public class TunnelController {

    private static String tunnelUrl = "";

    @PostMapping("/update")
    public ResponseEntity<?> updateTunnel(@RequestBody java.util.Map<String, String> body) {
        tunnelUrl = body.get("url");
        System.out.println("✅ Tunnel URL updated: " + tunnelUrl);
        return ResponseEntity.ok(java.util.Map.of("message", "Tunnel URL updated", "url", tunnelUrl));
    }

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentTunnel() {
        return ResponseEntity.ok(java.util.Map.of("url", tunnelUrl));
    }
}