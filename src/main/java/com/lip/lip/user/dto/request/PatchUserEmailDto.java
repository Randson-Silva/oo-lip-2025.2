package com.lip.lip.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record PatchUserEmailDto(
    @Email(message="Email is required")
    @NotBlank(message="Email is required")
    String email
) {
    
}
