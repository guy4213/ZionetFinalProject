package org.example.userpreferencesapp.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.userpreferencesapp.entity.Preference;

import java.util.LinkedHashSet;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private long id;
    private String userName;
    private String email;
    private LinkedHashSet<Preference> preferences;
}
