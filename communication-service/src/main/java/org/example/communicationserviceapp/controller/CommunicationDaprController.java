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
        return Mono.defer(() -> {
            try {
                NewsNotification newsNotification = cloudEvent.getData();
                if (newsNotification == null) {
                    logger.error("Received null news notification");
                    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("News notification is null"));
                }

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
    public ResponseEntity<String> handleNewsAggregationByUserId(@PathVariable long userId) {
        try {
            // Invoke the method on the news aggregation service
            NewsNotification newsNotification = daprClient.invokeMethod(
                    "news-aggregation-service",
                    "newsAggregation/dapr/subscribe/userDetails/" + userId,
                    null,
                    HttpExtension.GET,
                    NewsNotification.class  // Type of response expected
            ).block(); // Blocks until the request completes

            if (newsNotification != null) {
                communicationService.sendNewsNotification(newsNotification);
                return ResponseEntity.ok("Email sent successfully : "+newsNotification.getUser().getEmail());
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("News notification is null");
            }
        } catch (Exception e) {
            // Log the exception with detailed information
            logger.error("Error occurred while handling news aggregation for user ID {}: {}", userId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email: " + e.getMessage());
        }
    }
}
