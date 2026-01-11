package com.lip.lip.studylog.dtos.response;

import java.time.LocalDate;

import com.lip.lip.studylog.entity.StudyLog;

public record StudyLogResponseDto(
        Long id,
        String disciplineId,
        String timeSpent,
        LocalDate date,
        String topic,
        String notes) {
    public StudyLogResponseDto(StudyLog studyLog) {
        this(   
                studyLog.getId(),
                String.valueOf(studyLog.getDiscipline().getId()),
                convertMinutesToTimeFormat(studyLog.getDurationMinutes()),
                studyLog.getStudyDate(),
                studyLog.getTheme(),
                null // notes field doesn't exist in current entity, but keeping for frontend
                     // compatibility
        );
    }

    private static String convertMinutesToTimeFormat(Integer minutes) {
        if (minutes == null)
            return "00:00";
        int hours = minutes / 60;
        int mins = minutes % 60;
        return String.format("%02d:%02d", hours, mins);
    }
}