package com.lip.lip.revision.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public record RevisionUpdateRequest(
        @NotNull(message = "dueDate é obrigatório")
        LocalDate dueDate
) {}
