package org.example.userpreferencesapp.dto.user;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DeleteUserResponseDto {
    private boolean deleted;
    private UserResponse userResponse;
}
