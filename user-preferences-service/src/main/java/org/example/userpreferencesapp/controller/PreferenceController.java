package org.example.userpreferencesapp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.userpreferencesapp.dto.preference.PreferenceRequest;
import org.example.userpreferencesapp.dto.preference.PreferenceResponse;
import org.example.userpreferencesapp.service.PreferenceServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/preferences")
public class PreferenceController {
    private final PreferenceServiceImpl preferenceService;

    @PostMapping()
    public ResponseEntity<PreferenceResponse> addPreference
            (@Valid @RequestBody PreferenceRequest req,
             UriComponentsBuilder uriBuilder) {
        var res = preferenceService.addPreference(req);
        var uri = uriBuilder.path("/api/v1/preferences/{id}").buildAndExpand(res.getId()).toUri();
        return ResponseEntity.created(uri).body(res);
    }

    @GetMapping()
    public ResponseEntity<Collection<PreferenceResponse>> getAll() {

        var res = preferenceService.getAll();

        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PreferenceResponse> getPreferenceById(@PathVariable long id){
        return ResponseEntity.ok(preferenceService.getPreferenceById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PreferenceResponse> updatePreference(
            @PathVariable long id,
            @Valid @RequestBody PreferenceRequest req) throws Exception {

        return ResponseEntity.ok(preferenceService.update(id,req));
    }
}
