package org.example.newsaggregationapp.entity;

import lombok.Data;

import java.util.LinkedHashSet;
import java.util.Set;

@Data

public class User {

    private Long id;

    private String userName;

    private String email;

    private Set<Preference> preferences=new LinkedHashSet<>();
}
