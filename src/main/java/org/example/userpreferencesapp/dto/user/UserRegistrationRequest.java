package org.example.userpreferencesapp.dto.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.userpreferencesapp.entity.Preference;

import java.util.LinkedHashSet;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationRequest {
    @NotNull
    @NotEmpty(message = "User name cannot be empty")
    @Size(min = 2,max = 30)
    private String userName;

    @NotNull
    @NotEmpty(message = "Email cannot be empty")
    @Pattern(regexp = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$"
            ,message = "please enter valid email pattern ," +
            "for instance: guy42@gmail.com")
    private String email;


    @NotNull
    private String password;

    private LinkedHashSet<Preference> preferences;



}