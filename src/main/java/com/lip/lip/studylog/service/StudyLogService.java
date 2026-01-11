package com.lip.lip.studylog.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lip.lip.disicipline.entity.Discipline;
import com.lip.lip.disicipline.repository.DisiciplineRepository;
import com.lip.lip.revision.entity.Revision;
import com.lip.lip.revision.repository.RevisionRepository;
import com.lip.lip.studylog.dtos.request.StudyLogRegisterDto;
import com.lip.lip.studylog.dtos.response.StudyLogResponseDto;
import com.lip.lip.studylog.entity.StudyLog;
import com.lip.lip.studylog.repository.StudyLogRepository;
import com.lip.lip.user.entity.User;
import com.lip.lip.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudyLogService {

    private final StudyLogRepository studyLogRepository;
    private final RevisionRepository revisionRepository;
    private final UserRepository userRepository;
    private final DisiciplineRepository disciplineRepository;

    @Transactional
    public StudyLogResponseDto createStudy(StudyLogRegisterDto dto, Long userId) {
        // User user = userRepository.findById(userId)
        // .orElseThrow(() -> new RuntimeException("User not found"));

        // Discipline discipline = disciplineRepository.findById(dto.disciplineId())
        // .orElseThrow(() -> new RuntimeException("Discipline not found"));

        StudyLog log = new StudyLog();
        log.setTheme(dto.getTheme()); // Usa o alias
        log.setDurationMinutes(dto.getDurationMinutes()); // Converte tempo
        log.setStudyDate(dto.getStudyDate()); // Usa o alias
        // log.setDiscipline(discipline);
        // log.setUser(user);

        StudyLog savedLog = studyLogRepository.save(log);

        // Criar revisões automáticas
        List<Integer> revisionGaps = List.of(1, 7, 14);
        for (Integer days : revisionGaps) {
            Revision revision = new Revision();
            revision.setScheduledDate(savedLog.getStudyDate().plusDays(days));
            revision.setStudyLog(savedLog);
            revision.setUser(savedLog.getUser());
            revision.setCompleted(false);
            revisionRepository.save(revision);
        }

        return new StudyLogResponseDto(savedLog);
    }

    public List<StudyLogResponseDto> getAllStudies(Long userId) {
        List<StudyLog> studies = studyLogRepository.findByUserId(userId);

        return studies.stream()
                .map(StudyLogResponseDto::new)
                .collect(Collectors.toList());
    }

    public StudyLogResponseDto getStudyById(Long id, Long userId) {
        StudyLog study = studyLogRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Study not found"));

        return new StudyLogResponseDto(study);
    }

    @Transactional
    public StudyLogResponseDto updateStudy(Long id, StudyLogRegisterDto dto, Long userId) {
        StudyLog study = studyLogRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Study not found"));

        Discipline discipline = disciplineRepository.findById(dto.disciplineId())
                .orElseThrow(() -> new RuntimeException("Discipline not found"));

        study.setTheme(dto.topic());
        study.setDurationMinutes(convertTimeToMinutes(dto.timeSpent()));
        study.setStudyDate(dto.date());
        study.setDiscipline(discipline);

        studyLogRepository.save(study);
        return new StudyLogResponseDto(study);
    }

    @Transactional
    public void deleteStudy(Long id, Long userId) {
        StudyLog study = studyLogRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Study not found"));

        studyLogRepository.delete(study);
    }

    private Integer convertTimeToMinutes(String timeSpent) {
        String[] parts = timeSpent.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return (hours * 60) + minutes;
    }
}