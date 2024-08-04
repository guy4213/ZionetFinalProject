package org.example.communicationserviceapp.controller;

import io.dapr.Topic;
import io.dapr.client.DaprClient;
import io.dapr.client.domain.CloudEvent;
import io.dapr.client.domain.HttpExtension;
import org.example.communicationserviceapp.entity.NewsNotification;
import org.example.communicationserviceapp.service.CommunicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/dapr/subscribe")
public class CommunicationDaprController {

    private static final Logger logger = LoggerFactory.getLogger(CommunicationDaprController.class);

    private final CommunicationService communicationService;
    private final DaprClient daprClient;

    @Autowired
    public CommunicationDaprController(CommunicationService communicationService, DaprClient daprClient) {
        this.communicationService = communicationService;
        this.daprClient = daprClient;
    }

    // Endpoint that Dapr will call when a new message is added to the queue
    @Topic(name = "newsDetails", pubsubName = "newsfetch")
    @PostMapping("/newsNotificationDetails")
    public Mono<ResponseEntity<String>> handleRegisteredNewsNotificationDetails(@RequestBody CloudEvent<NewsNotification> cloudEvent) {
        LoggerFactory.getLogger(CommunicationDaprController.class).debug("entered handleUserRegistration method");
       //Mono- a stream that emits at most one item- successfully or with an error, async.
       //Mono.differ()- this method being excecuted only when subscriber subscribes to it.
       //its useful when you want to create new mono for each subscription.
        return Mono.defer(() -> {
            try {
                NewsNotification newsNotification = cloudEvent.getData();
                if (newsNotification == null) {
                    logger.error("Received null news notification");
                    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("News notification is null"));
                }
                //Mono.fromCallable-a factory method in Project Reactor that creates a Mono from a Callable task.
                // It's particularly useful when you want to execute a synchronous,
                // potentially blocking operation in a reactive context.
                return Mono.fromCallable(() -> {
                    try {
                        communicationService.sendNewsNotification(newsNotification);
                        return ResponseEntity.ok("Email sent successfully: " + newsNotification.getUser().getEmail());
                    } catch (Exception e) {
                        logger.error("Error sending email for user ID {}: {}", newsNotification.getUser().getId(), e.getMessage(), e);
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email: " + e.getMessage());
                    }
                });
            } catch (Exception e) {
                logger.error("Error processing news notification: {}", e.getMessage(), e);
                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing news notification: " + e.getMessage()));
            }
        }).onErrorResume(e -> {
            logger.error("Unexpected error in handleRegisteredNewsNotificationDetails: {}", e.getMessage(), e);
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred"));
        });
    }



    @GetMapping(path = "/newsNotification/byUserId/{userId}")
    public Mono<ResponseEntity<String>> handleNewsAggregationByUserId(@PathVariable long userId) {
        logger.info("Handling news aggregation for userId: {}", userId);
    
        return daprClient.invokeMethod(
                "news-aggregation-service",
                "newsAggregation/dapr/subscribe/userDetails/" + userId,
                null,
                HttpExtension.GET,
                NewsNotification.class
        ).
        flatMap(newsNotification -> {
            if (newsNotification == null) {
                //recieve null notification
                logger.warn("Received null news notification for userId: {}", userId);
                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("News notification is null"));
            }
            return Mono.fromCallable(() -> {
                communicationService.sendNewsNotification(newsNotification);
                return ResponseEntity.ok("Email sent successfully: " + newsNotification.getUser().getEmail());
            });
        }).onErrorResume(e -> {
            logger.error("Error occurred while sending  news for user ID {}: {}", userId, e.getMessage(), e);
            if (e instanceof IllegalArgumentException) {
                return Mono.just(ResponseEntity.badRequest().body("Invalid request: " + e.getMessage()));
            } else if (e instanceof IllegalStateException) {
                return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resource not found: " + e.getMessage()));
            } else {
                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to process request: " + e.getMessage()));
            }
        });
    }
}
