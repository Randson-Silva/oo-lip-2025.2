package com.lip.lip.user.dto.response;

import com.lip.lip.user.entity.User;

public record UserResponseDto(
    String name,
    String email
) {
    public UserResponseDto(User user){
        this(
            user.getName(),
            user.getEmail()
        );
    }
}
