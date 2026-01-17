package com.lip.lip.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PatchUserNameDto(
    @NotBlank(message="Name is required")
    String name
) {
    
}
