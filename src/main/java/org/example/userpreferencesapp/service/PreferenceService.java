package org.example.userpreferencesapp.service;

import org.example.userpreferencesapp.dto.preference.PreferenceRequest;
import org.example.userpreferencesapp.dto.preference.PreferenceResponse;

import java.util.Collection;

public interface PreferenceService {
     PreferenceResponse addPreference(PreferenceRequest req) ;
        //request dto=> category
     Collection<PreferenceResponse> getAll();
     PreferenceResponse getPreferenceById(long id) throws Exception;
}
