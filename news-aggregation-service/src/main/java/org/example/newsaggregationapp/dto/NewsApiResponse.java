package org.example.newsaggregationapp.dto;

import org.example.newsaggregationapp.entity.Article;

import java.util.List;

public class NewsApiResponse {
    private List<Article> results;

    public List<Article> getResults() {
        return results;
    }

    public void setResults(List<Article> results) {
        this.results = results;
    }
}

