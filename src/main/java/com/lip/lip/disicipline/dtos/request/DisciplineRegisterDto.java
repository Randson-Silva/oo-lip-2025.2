package com.lip.lip.disicipline.dtos.request;

import jakarta.validation.constraints.NotBlank;

public record DisciplineRegisterDto(
    @NotBlank(message="Name is required")
    String name,

    String color
) {
    
}
