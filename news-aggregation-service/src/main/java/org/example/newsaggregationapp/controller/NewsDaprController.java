package org.example.newsaggregationapp.controller;

import io.dapr.Topic;
import io.dapr.client.DaprClient;
import io.dapr.client.domain.CloudEvent;
import io.dapr.client.domain.HttpExtension;
import lombok.RequiredArgsConstructor;
import org.example.newsaggregationapp.entity.NewsNotification;
import org.example.newsaggregationapp.entity.User;
import org.example.newsaggregationapp.service.NewsService;
import org.slf4j.Logger;
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

    Logger logger = LoggerFactory.getLogger(NewsDaprController.class);

    @Topic(name = "userRegisterDetails", pubsubName = "pubsub")
    @PostMapping("/userRegisterDetails")
    public Mono<ResponseEntity<Object>> handleUserRegistration(@RequestBody CloudEvent<User> cloudEvent) {
       
        logger.debug("Entered handleUserRegistration method");
    
        Map<String, String> metadata = new HashMap<>();
        metadata.put("cloudevent.datacontenttype", "application/*+json");
    
        return Mono.fromCallable(() -> {
                    User user = cloudEvent.getData();
                    return newsService.aggregateNews(user);
                })
                .flatMap(notification -> daprClient.publishEvent("newsfetch", "newsDetails", notification, metadata)
                        .thenReturn(ResponseEntity.ok().body((Object) notification)))
                .onErrorResume(e -> {
                    logger.error("Error processing user registration", e);
                    ResponseEntity<Object> errorResponse;
                    if (e instanceof IllegalArgumentException) {
                        errorResponse = ResponseEntity.badRequest().body(e.getMessage());
                    } else if (e instanceof IllegalStateException) {
                        errorResponse = ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
                    } else {
                        errorResponse = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("An unexpected error occurred: " + e.getMessage());
                    }
                    return Mono.just(errorResponse);
                });
    }


   @GetMapping(path = "/userDetails/{userId}")
    public Mono<ResponseEntity<Object>> getUserDetailsById(@PathVariable long userId) {
        logger.info("Fetching user details for userId: {}", userId);

        return daprClient.invokeMethod(
                "user-preferences-service",
                "api/v1/users/" + userId,
                null,
                HttpExtension.GET,
                User.class
        ).flatMap(user -> {
            if (user == null) {
                logger.warn("User not found for userId: {}", userId);
                return Mono.just(ResponseEntity.notFound().build());
            }
            logger.debug("User found: {}", user);
            return Mono.just(newsService.aggregateNews(user))
                    .map(newsNotification -> ResponseEntity.ok().body((Object) newsNotification));
        }).onErrorResume(e -> {
            logger.error("Error fetching user details for userId: {}", userId, e);
            if (e instanceof IllegalArgumentException) {
                return Mono.just(ResponseEntity.badRequest().body("Invalid user preferences: " + e.getMessage()));
            } else if (e instanceof IllegalStateException) {
                return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body("No articles found: " + e.getMessage()));
            } else {
                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("An unexpected error occurred: " + e.getMessage()));
            }
        });
    }
}
