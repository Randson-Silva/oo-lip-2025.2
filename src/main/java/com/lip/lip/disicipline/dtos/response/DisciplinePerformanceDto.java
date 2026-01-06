package com.lip.lip.disicipline.dtos.response;

public record DisciplinePerformanceDto(
    String disciplineName,
    Long totalStudies,
    Long completedRevisions
) {}
