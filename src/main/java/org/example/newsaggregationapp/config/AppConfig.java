package org.example.newsaggregationapp.config;

import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration

public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Bean
    public DaprClient daprClient() {
        return new DaprClientBuilder().build(); // Example configuration, adjust as per your setup
    }

}



