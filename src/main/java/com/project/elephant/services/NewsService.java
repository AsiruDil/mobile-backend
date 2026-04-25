package com.project.elephant.services;

import com.project.elephant.models.News;
import com.project.elephant.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;

    public List<News> getAllNews() {
        return newsRepository.findAll();
    }

    public List<News> getEnabledNews() {
        return newsRepository.findByEnabledTrue();
    }

    public News createNews(String title, String description, String imageUrl) {
        News news = new News();
        news.setTitle(title);
        news.setDescription(description);
        news.setImageUrl(imageUrl);
        news.setEnabled(true);
        return newsRepository.save(news);
    }

    public News updateNews(String id, String title, String description, String imageUrl) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found with id: " + id));
        news.setTitle(title);
        news.setDescription(description);
        if (imageUrl != null) {
            news.setImageUrl(imageUrl);
        }
        return newsRepository.save(news);
    }

    public void deleteNews(String id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found with id: " + id));
        newsRepository.delete(news);
    }

    public News toggleNewsStatus(String id, boolean enabled) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found with id: " + id));
        news.setEnabled(enabled);
        return newsRepository.save(news);
    }
}