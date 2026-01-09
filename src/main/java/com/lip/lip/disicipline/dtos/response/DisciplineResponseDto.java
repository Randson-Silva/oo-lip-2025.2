package com.lip.lip.disicipline.dtos.response;

import com.lip.lip.user.entity.User;

public record DisciplineResponseDto(
    String name,
    User user,
    String color
) {
    
}
