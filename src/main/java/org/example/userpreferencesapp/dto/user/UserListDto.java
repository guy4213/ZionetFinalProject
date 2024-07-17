package org.example.userpreferencesapp.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class UserListDto {
    //pagination:
    private long totalUsers;
    private int pageNo;
    private int pageSize;
    private int totalPages;
    private boolean isFirst;
    private boolean isLast;

    private final Collection<UserResponse> series;
}
