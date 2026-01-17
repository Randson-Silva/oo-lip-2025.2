package com.lip.lip.subject.dto.request;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SubjectRegisterDto(
    
    @NotBlank(message = "Name is required.")
    @Size(max = 50, message = "Name must have a maximum of 50 characters")
    @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ\\s'-]{2,50}$", 
             message = "Name should contain only letters, spaces, apostrophes, or hyphens")
    String name,

    @NotBlank(message="Theme is required.")
    @Size(max=50, message="The theme must have a maxium of 50 characters.")
    @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ\\s'-]{2,50}$", message = "The name should contain only letters, spaces, apostrophes, or hyphens.")
    String theme,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    LocalDateTime studyTime,
    
    String color
) {}