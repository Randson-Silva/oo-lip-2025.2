package com.lip.lip.studylog.dtos.request;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StudyLogRegisterDto(
    @NotNull(message = "Discipline ID is required")
    Long disciplineId,

    @NotBlank(message = "Time spent is required (format: HH:mm)")
    String timeSpent,

    @NotNull(message = "Date is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate date,

    @NotBlank(message = "Topic is required")
    String topic,

    String notes
) { 
    // Converter timeSpent (HH:mm) para minutos
    public Integer getDurationMinutes() {
        String[] parts = timeSpent.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return (hours * 60) + minutes;
    }
    
    // Alias para studyDate
    public LocalDate getStudyDate() {
        return date;
    }
    
    // Alias para theme
    public String getTheme() {
        return topic;
    }
}