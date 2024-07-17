package org.example.userpreferencesapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "UQ_USER_NAME", columnNames = {"userName"}),
        @UniqueConstraint(name = "UQ_USER_EMAIL", columnNames = {"email"})
})

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //AUTO_INCREMENT @GeneratedValue
    private Long id;

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

    @ManyToMany(fetch = FetchType.EAGER)

    @JoinTable(
            name = "user_preference",
            joinColumns = {@JoinColumn(name = "User_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "Preference_id", referencedColumnName = "id")}
    )
    private Set<Preference> preferences=new LinkedHashSet<>();
}
