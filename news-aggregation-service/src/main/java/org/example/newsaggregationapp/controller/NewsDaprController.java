package org.example.newsaggregationapp.controller;

import io.dapr.Topic;
import io.dapr.client.DaprClient;
import io.dapr.client.domain.CloudEvent;
import io.dapr.client.domain.HttpExtension;
import lombok.RequiredArgsConstructor;
import org.example.newsaggregationapp.entity.NewsNotification;
import org.example.newsaggregationapp.entity.User;
import org.example.newsaggregationapp.service.NewsService;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("newsAggregation/dapr/subscribe")
@RequiredArgsConstructor
public class NewsDaprController {

    private final NewsService newsService;

    private final DaprClient daprClient;
    @Topic(name = "userRegisterDetails", pubsubName = "pubsub")
    @PostMapping("/userRegisterDetails")
    public Mono<Object> handleUserRegistration(@RequestBody CloudEvent<User> cloudEvent) {
        LoggerFactory.getLogger(NewsDaprController.class).debug("entered handleUserRegistration method");
        Map<String, String> metadata = new HashMap<>();
        metadata.put("cloudevent.datacontenttype", "application/*+json");
        return Mono.fromCallable(() -> {
                    User user = cloudEvent.getData();
                    return newsService.aggregateNews(user);
                })
                .flatMap(notification -> {
                    if (notification == null) {
                        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body((NewsNotification) null));
                    }
                    return daprClient.publishEvent("newsfetch", "newsDetails", notification,metadata);

                })
                .onErrorResume(e -> {
                    LoggerFactory.getLogger(NewsDaprController.class).error("Error processing user registration", e);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body((NewsNotification) null));
                });
    }



    @GetMapping(path = "/userDetails/{userId}")
    public NewsNotification getUserDetailsById(@PathVariable long userId) {
        try {
            // Assuming UserPreferences service exposes an HTTP GET endpoint like "/users/{id}"
            User user = daprClient.invokeMethod(
                    "user-preferences-service",
                    "api/v1/users/" + userId,
                    null,
                    HttpExtension.GET,
                    User.class
            ).block(); // Blocks until the request completes (synchronous), or use subscribe() for async

            System.out.println(user);

            if (user != null) {
                return newsService.aggregateNews(user);
            }
            return null;
        } catch (Exception e) {
            // Log the error details to the console
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        }
    }
}