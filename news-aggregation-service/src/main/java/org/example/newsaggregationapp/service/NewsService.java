package org.example.newsaggregationapp.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.example.newsaggregationapp.dto.NewsApiResponse;
import org.example.newsaggregationapp.entity.Article;
import org.example.newsaggregationapp.entity.NewsNotification;
import org.example.newsaggregationapp.entity.Preference;
import org.example.newsaggregationapp.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Getter
@Setter
@RequiredArgsConstructor
public class NewsService {


    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${newsDataIo.api.url}")
    private String apiUrl;
    // Constructor with AmqpTemplate injection


    public NewsNotification aggregateNews(User user) {
        // Fetch user preferences
        Set<Preference> preferences = user.getPreferences();
    
        // Check for null or empty preferences
        if (preferences == null || preferences.isEmpty()) {
            throw new IllegalArgumentException("User preferences cannot be null or empty.");
        }
    
        // Fetch news articles based on preferences
        List<Article> articles = preferences.stream()
                .flatMap(preference -> fetchArticles(preference.getName()).stream())
                .collect(Collectors.toList());
    
        // Handle case where no articles are found
        if (articles.isEmpty()) {
            throw new IllegalStateException("No articles found for the given preferences.(Api problem)");
        }
    
        // Create NewsNotification object
        NewsNotification newsNotification = new NewsNotification(user, articles);
    
        // Return the news notification
        return newsNotification;
    }
    

    private List<Article> fetchArticles(String preference) {
        String url = apiUrl + "&category=" + preference;
        System.out.println(url);
        NewsApiResponse response = restTemplate.getForObject(url, NewsApiResponse.class);
        System.out.println(response);

        // Return the first 3 articles if available
        if (response != null && response.getResults() != null) {
            return response.getResults().stream()
                    .limit(3)
                    .collect(Collectors.toList());
        }

        return List.of();
    }
}
