package org.example.userpreferencesapp.service;

import org.example.userpreferencesapp.dto.user.UserListDto;
import org.example.userpreferencesapp.dto.user.UserRegistrationRequest;
import org.example.userpreferencesapp.dto.user.UserResponse;
import org.springframework.web.server.ResponseStatusException;

public interface UserService {
UserResponse registerUser(UserRegistrationRequest request, String[] preferences) throws ResponseStatusException;
     //
UserResponse updatePreferences(long userId, String[] preferences);
UserListDto getAll(int pageNo, int pageSize, String sortDir, String... sortBy);

UserResponse getUserById(long id);

}
