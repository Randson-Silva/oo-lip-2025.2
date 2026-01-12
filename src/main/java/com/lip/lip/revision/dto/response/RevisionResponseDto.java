package com.lip.lip.revision.dto.response;

import java.time.LocalDate;

import com.lip.lip.revision.entity.Revision;

public record RevisionResponseDto(
        Long id,
        Long studyRecordId,
        String disciplineId,
        String disciplineName,
        String topic,
        LocalDate dueDate,
        boolean completed,
        LocalDate completedAt,
        Integer revisionNumber) {

    public RevisionResponseDto(Revision revision) {
        this(
                revision.getId(),
                revision.getStudyLog().getId(),
                String.valueOf(revision.getStudyLog().getDiscipline().getId()),
                revision.getStudyLog().getDiscipline().getName(),
                revision.getStudyLog().getTheme(),
                revision.getScheduledDate(),
                revision.isCompleted(),
                revision.getCompletedAt(),
                revision.getRevisionNumber());
    }
}