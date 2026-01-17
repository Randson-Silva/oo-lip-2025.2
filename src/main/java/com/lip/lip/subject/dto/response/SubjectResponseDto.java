package com.lip.lip.subject.dto.response;

import com.lip.lip.subject.entity.Subject;

public record SubjectResponseDto(
    Long id,
    String name,
    String color,
    boolean active
) {
    public SubjectResponseDto(Subject subject) {
        this(
            subject.getId(),
            subject.getName(),
            subject.getColor(),
            subject.isActive()
        );
    }
}