package org.example.userpreferencesapp.service;

import lombok.RequiredArgsConstructor;
import org.example.userpreferencesapp.dto.preference.PreferenceRequest;
import org.example.userpreferencesapp.dto.preference.PreferenceResponse;
import org.example.userpreferencesapp.entity.Preference;
import org.example.userpreferencesapp.error.ResourceNotFoundException;
import org.example.userpreferencesapp.repository.PreferencesRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PreferenceServiceImpl implements PreferenceService{
    private final PreferencesRepository preferencesRepository;
    private final ModelMapper modelMapper;

    @Override
    public PreferenceResponse addPreference(PreferenceRequest req) {

        if ( req.getName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You've entered an empty string. Please enter a value.");
        }
      else if (preferencesRepository.findPreferenceByNameIgnoreCase(req.getName())!=null){
            throw new ResponseStatusException(HttpStatus.CONFLICT,req.getName()+" is already exist");
        }
        //request dto=> category
        var preferenceEntity = modelMapper.map(req, Preference.class);
        //Series->saved category
        var saved = preferencesRepository.save(preferenceEntity);
        //saved category->Response Dto
        return modelMapper.map(saved, PreferenceResponse.class);
    }

    @Override
    public Collection<PreferenceResponse> getAll() {
        List<Preference> preferences = preferencesRepository.findAll();

        // Sort preferences by ID
        List<PreferenceResponse> sortedPreferences = preferences.stream()
                .sorted(Comparator.comparingLong(Preference::getId))
                .map(preference -> modelMapper.map(preference, PreferenceResponse.class))
                .collect(Collectors.toList());

        return sortedPreferences;
    }

    @Override
    public PreferenceResponse getPreferenceById(long id) {
        Preference category = getPreferenceByIdOrThrow(id);

        // Ensure that the series set is loaded before mapping to DTO
//        System.out.println(category.getSeries().size());

        return modelMapper.map(category, PreferenceResponse.class);
    }
    public PreferenceResponse update(long id, PreferenceRequest req) throws Exception{

        Preference preference = getPreferenceByIdOrThrow(id);
        //update:
        if (req.getName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You've entered an empty string. Please enter a value.");
        }
       else if (preferencesRepository.findPreferenceByNameIgnoreCase(req.getName())!=null){
            throw new ResponseStatusException(HttpStatus.CONFLICT,req.getName()+" is already exist");
        }

       else if(req.getName()!=null&&!req.getName().trim().isEmpty()){
            preference.setName(req.getName());
        }


        var saved = preferencesRepository.save(preference);
        return modelMapper.map(saved, PreferenceResponse.class);
    }
    Preference getPreferenceByIdOrThrow(long id) {
        return preferencesRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Preference", "id", id)
        );
    }
}
