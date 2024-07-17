package org.example.communicationserviceapp.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


@Data
@NoArgsConstructor // Adding default constructor
@AllArgsConstructor
public class NewsNotification implements Serializable {
    @JsonSerialize
    @JsonDeserialize // Ensure User can be deserialized
    private User user;

    @JsonSerialize
    @JsonDeserialize // Ensure List<Article> can be deserialized
    private List<Article> articles;
}
