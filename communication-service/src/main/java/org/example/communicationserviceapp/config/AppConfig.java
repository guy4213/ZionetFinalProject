package org.example.communicationserviceapp.config;

import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public DaprClient daprClient() {
        return new DaprClientBuilder().build(); // Example configuration, adjust as per your setup
    }
}
