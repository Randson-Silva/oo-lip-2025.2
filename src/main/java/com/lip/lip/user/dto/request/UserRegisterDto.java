package com.lip.lip.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRegisterDto(

    @NotBlank(message="Name is required")
    String name,

    @Email(message="Email is required")
    @NotBlank(message="Email is required")
    String email,

    @NotBlank(message="Password is required")
    String password
) {
    
}
