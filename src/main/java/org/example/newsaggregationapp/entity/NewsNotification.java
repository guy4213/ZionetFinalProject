package org.example.newsaggregationapp.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
@AllArgsConstructor
public class NewsNotification implements Serializable {
    @JsonSerialize
    private User user;
    @JsonSerialize
    private List<Article> articles;
}
