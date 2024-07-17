package org.example.communicationserviceapp.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Article {
    @JsonProperty("article_id")
    private String articleId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("link")
    private String link;
    @JsonProperty("category")
    private List<String> category;


}