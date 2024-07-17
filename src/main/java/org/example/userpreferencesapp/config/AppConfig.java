package org.example.userpreferencesapp.config;

import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
    @Bean
    public DaprClient daprClient() {
        return new DaprClientBuilder().build(); // Example configuration, adjust as per your setup
    }
}



