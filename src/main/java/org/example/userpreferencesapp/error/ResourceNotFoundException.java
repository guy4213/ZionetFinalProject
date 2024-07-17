package org.example.userpreferencesapp.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND )
public class ResourceNotFoundException extends RuntimeException {
    private final String message;
    private final String resourceName;
    private final String name;
    private final Object value;

    public ResourceNotFoundException(String resourceName, String name, Object value) {
        this(resourceName, name, value, "can't be found");
    }

    public ResourceNotFoundException(String resourceName, String name, Object value, String message) {
        super("%s with %s = %s %s".formatted(resourceName, name, value, message));
        this.resourceName = resourceName;
        this.name = name;
        this.value = value;
        this.message = message;
    }
}