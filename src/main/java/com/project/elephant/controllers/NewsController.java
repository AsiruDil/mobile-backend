package com.project.elephant.controllers;

import com.project.elephant.dto.response.MessageResponse;
import com.project.elephant.models.News;
import com.project.elephant.services.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class NewsController {

    @Autowired
    private NewsService newsService;

    @GetMapping("/api/admin/news")
    public ResponseEntity<List<News>> getAllNews() {
        return ResponseEntity.ok(newsService.getAllNews());
    }

    @PostMapping("/api/admin/news")
    public ResponseEntity<News> createNews(@RequestBody Map<String, String> body) {
        return ResponseEntity.ok(newsService.createNews(
                body.get("title"),
                body.get("description"),
                body.get("imageUrl")
        ));
    }

    @PutMapping("/api/admin/news/{id}")
    public ResponseEntity<News> updateNews(
            @PathVariable String id,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(newsService.updateNews(
                id,
                body.get("title"),
                body.get("description"),
                body.get("imageUrl")
        ));
    }

    @DeleteMapping("/api/admin/news/{id}")
    public ResponseEntity<MessageResponse> deleteNews(@PathVariable String id) {
        newsService.deleteNews(id);
        return ResponseEntity.ok(new MessageResponse("News deleted successfully"));
    }

    @PatchMapping("/api/admin/news/{id}/status")
    public ResponseEntity<News> toggleStatus(
            @PathVariable String id,
            @RequestBody Map<String, Boolean> body) {
        return ResponseEntity.ok(newsService.toggleNewsStatus(
                id,
                body.getOrDefault("enabled", true)
        ));
    }

    // Public endpoint — mobile app uses this (no token needed)
    @GetMapping("/api/news")
    public ResponseEntity<List<News>> getPublicNews() {
        return ResponseEntity.ok(newsService.getEnabledNews());
    }
}