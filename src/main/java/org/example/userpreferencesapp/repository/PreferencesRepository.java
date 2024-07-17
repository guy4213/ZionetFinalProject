package org.example.userpreferencesapp.repository;

import org.example.userpreferencesapp.entity.Preference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreferencesRepository extends JpaRepository<Preference,Long> {
    Preference  findPreferenceByNameIgnoreCase(String preferenceName);
}
