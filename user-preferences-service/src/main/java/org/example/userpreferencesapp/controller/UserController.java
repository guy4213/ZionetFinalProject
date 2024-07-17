package org.example.userpreferencesapp.controller;

import io.dapr.client.DaprClient;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.userpreferencesapp.dto.user.UserListDto;
import org.example.userpreferencesapp.dto.user.UserRegistrationRequest;
import org.example.userpreferencesapp.dto.user.UserResponse;
import org.example.userpreferencesapp.error.UserServiceException;
import org.example.userpreferencesapp.service.UserServiceImpl;
import org.modelmapper.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")

public class UserController {

    private final UserServiceImpl userService;
    private final DaprClient daprClient;


    @PostMapping("/register")
    public Mono<ResponseEntity<Object>> registerUser(@Valid @RequestBody UserRegistrationRequest request,
                                                     @RequestParam(value = "preferencesNames", required = true) String... preferencesNames) {
        if (preferencesNames.length > 5) {
            return Mono.just(ResponseEntity.badRequest().body("Please enter maximum 5 preferences."));
        }
        Map<String, String> metadata = new HashMap<>();
        metadata.put("cloudevent.datacontenttype", "application/*+json");
        return Mono.fromCallable(() -> userService.registerUser(request, preferencesNames))
                .flatMap(savedUser ->
                        daprClient.publishEvent("pubsub", "userRegisterDetails", savedUser, metadata)

                )
                .then(Mono.fromCallable(() -> ResponseEntity.ok().body((Object)"User registered and details sent to queue")))
                .onErrorResume(ValidationException.class, ex ->
                        Mono.just(ResponseEntity.badRequest().body(ex.getMessage()))
                )
                .onErrorResume(UserServiceException.class, ex ->
                        Mono.just(ResponseEntity.badRequest().body("User service encountered an error: " + ex.getMessage()))
                )
                .onErrorResume(Exception.class, ex ->
                        Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + ex.getMessage()))
                );
    }

    @PutMapping("/updatePreferencesByNames")
    public ResponseEntity<?> UpdatePreferences(
            @RequestParam(value = "userId", required = true) long userId,
            @RequestParam(value = "preferencesNames", required = true) String... preferencesNames) {
        try {
            UserResponse res = userService.UpdatePreferences(userId, preferencesNames);
            return ResponseEntity.ok(res);
        } catch (IllegalArgumentException e) {
            String errorMessage = e.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
    }

    @GetMapping
    public ResponseEntity<UserListDto> getAll(
            @RequestParam(value = "pageNo", required = false, defaultValue = "0") int pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir,
            @RequestParam(value = "sortBy", required = false, defaultValue = "id") String... sortBy


    ) {

        var res = userService.getAll(pageNo, pageSize, sortDir, sortBy);

        return ResponseEntity.ok(res);

    }
    @GetMapping(value = "/{id}")
        public ResponseEntity<UserResponse> getUserById(@PathVariable long id) {
        System.out.println(userService.getUserById(id));
        return ResponseEntity.ok(userService.getUserById(id));
    }
}




















//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/v1/users")
//@CrossOrigin(origins={ "http://localhost:5173", "http://localhost:5174","http://localhost:3000"})
//
//public class UserController {
//
//    private final UserServiceImpl userService;
//
//    @PostMapping("/register")
//    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody  UserRegistrationRequest request)
//            throws Exception{
//        var registeredUser = userService.registerUser(request);
//        return ResponseEntity.ok(registeredUser);
//    }
//
//    @PostMapping("/addPreferencesByNames")
//    public ResponseEntity<?> setOrUpdatePreferences(
//            @RequestParam(value = "userId", required = true) long userId,
//            @RequestParam(value = "preferencesNames", required = true) String... preferencesNames) {
//        try {
//            UserResponse res = userService.setOrUpdatePreferences(userId, preferencesNames);
//            return ResponseEntity.ok(res);
//        } catch (IllegalArgumentException e) {
//            String errorMessage = e.getMessage();
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
//        }
//    }
//
//    @GetMapping
//    public ResponseEntity<UserListDto> getAll(
//            @RequestParam(value = "pageNo", required = false, defaultValue = "0") int pageNo,
//            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
//            @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir,
//            @RequestParam(value = "sortBy", required = false, defaultValue = "id") String... sortBy
//
//
//    ) {
//
//        var res = userService.getAll(pageNo, pageSize, sortDir, sortBy);
//
//        return ResponseEntity.ok(res);
//
//    }
//    @GetMapping(value = "/{id}")
//    public ResponseEntity<UserResponse> getUserById(@PathVariable long id) {
//        System.out.println(userService.getUserById(id));
//        return ResponseEntity.ok(userService.getUserById(id));
//    }
