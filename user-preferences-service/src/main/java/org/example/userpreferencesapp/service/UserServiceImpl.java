package org.example.userpreferencesapp.service;
import lombok.RequiredArgsConstructor;

import org.example.userpreferencesapp.dto.user.DeleteUserResponseDto;
import org.example.userpreferencesapp.dto.user.UserListDto;
import org.example.userpreferencesapp.dto.user.UserRegistrationRequest;
import org.example.userpreferencesapp.dto.user.UserResponse;
import org.example.userpreferencesapp.entity.Preference;
import org.example.userpreferencesapp.entity.User;
import org.example.userpreferencesapp.error.PaginationException;
import org.example.userpreferencesapp.error.ResourceNotFoundException;
import org.example.userpreferencesapp.repository.PreferencesRepository;
import org.example.userpreferencesapp.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PreferencesRepository preferencesRepository;

    public UserResponse registerUser(UserRegistrationRequest request, String[] preferences)
    throws ResponseStatusException {
    // Check if the username already exists
    if (userRepository.findUserByUserNameIgnoreCase(request.getUserName()) != null) {
        throw new ResponseStatusException(HttpStatus.CONFLICT, request.getUserName() + " already exists");
    }

    // Check if the email already exists
    if (userRepository.findUserByEmailIgnoreCase(request.getEmail()) != null) {
        throw new ResponseStatusException(HttpStatus.CONFLICT, request.getEmail() + " already exists");
    }

    // Fetch and sort preferences based on provided names
    var sortedSet = sortListToSet(preferences);
    // Convert sortedSet to LinkedHashSet for consistent ordering
    LinkedHashSet<Preference> sortedPreferences = new LinkedHashSet<>(sortedSet);
    // Set sorted preferences into the registration request
    request.setPreferences(sortedPreferences);
    // Map the request to the User entity and save it
    User savedUser = modelMapper.map(request, User.class);
    userRepository.save(savedUser);
    // Map the saved User entity to UserResponse and return it
    return mapUserToUserResponse(savedUser);
}


//update preferences(adding new to existing preferences-like PATCH Request)
    public UserResponse updatePreferences(long userId, String[] preferences) {
        User userEntity = getUserByIdOrThrow(userId);
       var sortedSet= sortListToSet(preferences);
        userEntity.getPreferences().addAll(sortedSet);
        var saved = userRepository.save(userEntity);
        return mapUserToUserResponse(saved);
    }

    //edit preferences(modifies the current preferencs- like PUT Request)
    public UserResponse editPreferences(long userId, String[] preferences) {
        User userEntity = getUserByIdOrThrow(userId);
       var sortedSet= sortListToSet(preferences);
        userEntity.setPreferences(sortedSet);
        var saved = userRepository.save(userEntity);
        return mapUserToUserResponse(saved);
    }
//sorting list to set & checkung if all preferences being set by the user do exist 
    private Set<Preference> sortListToSet(String[] preferences) {
        List<String> missingPreferences = new ArrayList<>();
        List<Preference> sortedList = Arrays.stream(preferences)
                .map(preferenceName -> {
                    Preference preference = preferencesRepository.findPreferenceByNameIgnoreCase(preferenceName);
                    if (preference == null) {
                        missingPreferences.add(preferenceName);
                    }
                    return preference;
                })
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(Preference::getId))
                .collect(Collectors.toList());
    
        // Check if any preferences were not found & returning them.
        if (!missingPreferences.isEmpty()) {
            String missingPreferencesString = String.join(", ", missingPreferences);
            throw new NullPointerException("The following preferences were not found: " + missingPreferencesString);
        }
    
        return new HashSet<>(sortedList);
    }
    @Override
    public UserListDto getAll(int pageNo, int pageSize, String sortDir, String... sortBy) {
        try {
            // Direction from String ('asc/desc')
            Sort.Direction sort = Sort.Direction.fromString(sortDir);
            // Build the page request
            var pageable = PageRequest.of(pageNo, pageSize, sort, sortBy);

            // Get the page result from the repository
            Page<User> pr = userRepository.findAll(pageable);

            if (pageNo >= pr.getTotalPages()) {
                throw new PaginationException("Page number " + pageNo + " exceeds total pages " + pr.getTotalPages());
            }

            // Map users to UserResponse
            List<UserResponse> userList = pr.getContent().stream()
                    .map(this::mapUserToUserResponse) // Use method reference or lambda for mapping
                    .toList();

            return new UserListDto(
                    pr.getTotalElements(),
                    pr.getNumber(),
                    pr.getSize(),
                    pr.getTotalPages(),
                    pr.isFirst(),
                    pr.isLast(),
                    userList
            );
        } catch (IllegalArgumentException e) {
            throw new PaginationException(e.getMessage());
        }
    }
//sorting the preferences by id when transferring user entity to its response entity
//(using linkedHashset);
    private UserResponse mapUserToUserResponse(User user) {
        // Map user to UserResponse, handling preferences if they are null or empty
        UserResponse userResponse = modelMapper.map(user, UserResponse.class);
        // Sort preferences by id
        if (user.getPreferences() != null) {
            List<Preference> sortedPreferences = user.getPreferences().stream()
                    .sorted(Comparator.comparingLong(Preference::getId))
                    .toList();
            userResponse.setPreferences(new LinkedHashSet<>(sortedPreferences));
        } else {
            userResponse.setPreferences(new LinkedHashSet<>());
        }

        return userResponse;
    }
    public UserResponse getUserById(long id) {
        User user = getUserByIdOrThrow(id);
        return mapUserToUserResponse(user);
    }
    User getUserByIdOrThrow(long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", "" + id, " does not exist")

        );
    }
    public DeleteUserResponseDto deleteUserById(long id) {
        //check for existence before deleting:
        var user = userRepository.findById(id);

        //delete:
        userRepository.deleteById(id);

        //return true if existed before deletion:
        return DeleteUserResponseDto.builder()
        .deleted(user.isPresent()).userResponse(modelMapper.map(user, UserResponse.class)).build();
}
}








































//
//
//
//package org.example.userpreferencesapp.service;
//
//import lombok.RequiredArgsConstructor;
//import org.example.userpreferencesapp.dto.user.UserListDto;
//import org.example.userpreferencesapp.dto.user.UserRegistrationRequest;
//import org.example.userpreferencesapp.dto.user.UserResponse;
//import org.example.userpreferencesapp.entity.Preference;
//import org.example.userpreferencesapp.entity.User;
//import org.example.userpreferencesapp.error.PaginationException;
//import org.example.userpreferencesapp.error.ResourceNotFoundException;
//import org.example.userpreferencesapp.repository.PreferencesRepository;
//import org.example.userpreferencesapp.repository.UserRepository;
//import org.modelmapper.ModelMapper;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Sort;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.util.*;
//
//@Service
//@RequiredArgsConstructor
//public class UserServiceImpl implements UserService{
//
//    private final UserRepository userRepository;
//    private final ModelMapper modelMapper;
//    private final PreferencesRepository preferencesRepository;
//
//    public UserResponse registerUser(UserRegistrationRequest request) throws ResponseStatusException {
//        // Check if the username already exists
//        if (userRepository.findUserByUserNameIgnoreCase(request.getUserName()) != null) {
//            throw new ResponseStatusException(HttpStatus.CONFLICT, request.getUserName() + " already exists");
//        }
//
//        // Check if the email already exists
//        else if (userRepository.findUserByEmailIgnoreCase(request.getEmail()) != null) {
//            throw new ResponseStatusException(HttpStatus.CONFLICT, request.getEmail() + " already exists");
//        }
//        else {
//            // Map the request to the User entity and save it
//            var savedUser = modelMapper.map(request, User.class);
//            userRepository.save(savedUser);
//
//            // Map the saved User entity to UserResponse and return it
//            return modelMapper.map(savedUser, UserResponse.class);
//        }
//    }
//
//
//    public UserResponse setOrUpdatePreferences(long userId, String[] preferences) {
//        User userEntity = getUserByIdOrThrow(userId);
//        List<Preference> sortedList = Arrays.stream(preferences)
//                .map(preferencesRepository::findPreferenceByNameIgnoreCase)
//                .sorted(Comparator.comparing(Preference::getId))
//                .toList();
//        Set<Preference> sortedSet = new HashSet<>(sortedList);
//        // Check if any null elements were filtered out
//        if (sortedList.size() < preferences.length) {
//            throw new IllegalArgumentException("One or more preferences not found");
//        }
//
//        userEntity.getPreferences().addAll(sortedSet);
//
//        var saved = userRepository.save(userEntity);
//
//        return mapUserToUserResponse(saved);
//    }
//
//    @Override
//    public UserListDto getAll(int pageNo, int pageSize, String sortDir, String... sortBy) {
//        try {
//            // Direction from String ('asc/desc')
//            Sort.Direction sort = Sort.Direction.fromString(sortDir);
//            // Build the page request
//            var pageable = PageRequest.of(pageNo, pageSize, sort, sortBy);
//
//            // Get the page result from the repository
//            Page<User> pr = userRepository.findAll(pageable);
//
//            if (pageNo >= pr.getTotalPages()) {
//                throw new PaginationException("Page number " + pageNo + " exceeds total pages " + pr.getTotalPages());
//            }
//
//            // Map users to UserResponse
//            List<UserResponse> userList = pr.getContent().stream()
//                    .map(this::mapUserToUserResponse) // Use method reference or lambda for mapping
//                    .toList();
//
//            return new UserListDto(
//                    pr.getTotalElements(),
//                    pr.getNumber(),
//                    pr.getSize(),
//                    pr.getTotalPages(),
//                    pr.isFirst(),
//                    pr.isLast(),
//                    userList
//            );
//        } catch (IllegalArgumentException e) {
//            throw new PaginationException(e.getMessage());
//        }
//    }
//    //sorting the preferences by id when transferring user entity to its response entity
////(using linkedHashset);
//    private UserResponse mapUserToUserResponse(User user) {
//        // Map user to UserResponse, handling preferences if they are null or empty
//        UserResponse userResponse = modelMapper.map(user, UserResponse.class);
//
//        // Sort preferences by id
//        if (user.getPreferences() != null) {
//            List<Preference> sortedPreferences = user.getPreferences().stream()
//                    .sorted(Comparator.comparingLong(Preference::getId))
//                    .toList();
//            userResponse.setPreferences(new LinkedHashSet<>(sortedPreferences));
//        } else {
//            userResponse.setPreferences(new LinkedHashSet<>());
//        }
//
//        return userResponse;
//    }
//    public UserResponse getUserById(long id) {
//        User user = getUserByIdOrThrow(id);
//        return mapUserToUserResponse(user);
//    }
//    User getUserByIdOrThrow(long id) {
//        return userRepository.findById(id).orElseThrow(
//                () -> new ResourceNotFoundException("User", "id", "" + id, " does not exist")
//
//        );
//    }
//}
//
//
//
//
//
