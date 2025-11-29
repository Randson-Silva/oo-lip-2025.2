package com.lip.lip.subject.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SubjectRegisterDto(
    
    @NotBlank(message = "Name is required")
    @Size(max = 50, message = "Name must have a maximum of 50 characters")
    @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ\\s'-]{2,50}$", 
             message = "Name should contain only letters, spaces, apostrophes, or hyphens")
    String name,
    
    String color
) {}