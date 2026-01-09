package com.lip.lip.studylog.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record StudyLogRegisterDto(
    @NotBlank(message = "Theme is required")
    String theme,

    @NotNull(message = "Duration is required")
    @Positive(message = "Duration must be positive")
    Integer durationMinutes,

    @NotNull(message = "Discipline ID is required")
    Long disciplineId
) { }
