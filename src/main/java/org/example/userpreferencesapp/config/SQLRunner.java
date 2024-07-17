package org.example.userpreferencesapp.config;


import lombok.RequiredArgsConstructor;
import org.example.userpreferencesapp.entity.Preference;
import org.example.userpreferencesapp.entity.User;
import org.example.userpreferencesapp.repository.PreferencesRepository;
import org.example.userpreferencesapp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
@RequiredArgsConstructor
public class SQLRunner implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PreferencesRepository preferencesRepository;

    @Override
    public void run(String... args) throws Exception {

        // Save preferences with unique instances
        Preference business = preferencesRepository.save(Preference.builder().id(1L).name("Business").build());
        Preference crime = preferencesRepository.save(Preference.builder().id(2L).name("Crime").build());
        Preference domestic = preferencesRepository.save(Preference.builder().id(3L).name("Domestic").build());
        Preference education = preferencesRepository.save(Preference.builder().id(4L).name("Education").build());
        Preference entertainment = preferencesRepository.save(Preference.builder().id(5L).name("Entertainment").build());
        Preference  environment = preferencesRepository.save(Preference.builder().id(6L).name("Environment").build());
        Preference  food = preferencesRepository.save(Preference.builder().id(7L).name("Food").build());
        Preference  health = preferencesRepository.save(Preference.builder().id(8L).name("Health").build());
        Preference  lifestyle = preferencesRepository.save(Preference.builder().id(9L).name("Lifestyle").build());
        Preference   other = preferencesRepository.save(Preference.builder().id(10L).name("Other").build());
        Preference  politics = preferencesRepository.save(Preference.builder().id(11L).name("Politics").build());
        Preference  science = preferencesRepository.save(Preference.builder().id(12L).name("Science").build());
        Preference   sports = preferencesRepository.save(Preference.builder().id(13L).name("Sports").build());
        Preference    technology = preferencesRepository.save(Preference.builder().id(14L).name("Technology").build());
        Preference   top = preferencesRepository.save(Preference.builder().id(15L).name("Top").build());
        Preference   tourism = preferencesRepository.save(Preference.builder().id(16L).name("Tourism").build());
        Preference    world = preferencesRepository.save(Preference.builder().id(17L).name("World").build());

        // Comparator based on id
        Comparator<Preference> preferenceComparator = Comparator.comparing(Preference::getId);

        // Create sorted preferences set for each user
        Set<Preference> sortedPreferences1 = new TreeSet<>(preferenceComparator);
        sortedPreferences1.addAll(Arrays.asList(business, crime,domestic,education));

        Set<Preference> sortedPreferences2 = new TreeSet<>(preferenceComparator);
        sortedPreferences2.addAll(Arrays.asList(business, crime,domestic,education));

        // Save user1 with sorted preferences
        var user1 = userRepository.save(
                User.builder()
                        .id(1L)
                        .userName("guy421367")
                        .email("guy421367@gmail.com")
                        .password("Guy72@000")
                        .preferences(new HashSet<>(sortedPreferences1)) // Use new HashSet to ensure distinct reference
                        .build()
        );

        // Save user2 with sorted preferences
        var user2 = userRepository.save(
                User.builder()
                        .id(2L)
                        .userName("guywork421367")
                        .email("guywork421367@gmail.com")
                        .password("ofek122000")
                        .preferences(new HashSet<>(sortedPreferences2)) // Use new HashSet to ensure distinct reference
                        .build()
        );
    }
}
