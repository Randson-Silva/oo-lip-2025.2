package com.lip.lip.studylog.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lip.lip.disicipline.entity.Discipline;
import com.lip.lip.disicipline.repository.DisiciplineRepository;
import com.lip.lip.revision.service.RevisionService;
import com.lip.lip.studylog.dtos.request.StudyLogRegisterDto;
import com.lip.lip.studylog.dtos.response.StudyLogResponseDto;
import com.lip.lip.studylog.entity.StudyLog;
import com.lip.lip.studylog.repository.StudyLogRepository;
import com.lip.lip.user.entity.User;
import com.lip.lip.user.entity.UserSettings;
import com.lip.lip.user.repository.UserRepository;
import com.lip.lip.user.repository.UserSettingsRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudyLogService {

    private final StudyLogRepository studyLogRepository;
    private final DisiciplineRepository disciplineRepository;
    private final UserRepository userRepository;
    private final RevisionService revisionService;
    private final UserSettingsRepository userSettingsRepository;

    @Transactional
    public StudyLogResponseDto createStudy(StudyLogRegisterDto dto, Long userId) {
        log.info("Criando novo estudo para usuário: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Discipline discipline = disciplineRepository.findById(dto.disciplineId())
                .orElseThrow(() -> new RuntimeException("Disciplina não encontrada"));

        // Buscar configurações do usuário
        UserSettings settings = userSettingsRepository.findByUser(user)
                .orElseGet(() -> createDefaultSettings(user));

        StudyLog studyLog = new StudyLog();
        studyLog.setUser(user);
        studyLog.setDiscipline(discipline);
        studyLog.setStudyDate(dto.date());
        studyLog.setTheme(dto.topic());
        studyLog.setDurationMinutes(dto.getDurationMinutes());
        studyLog.setNotes(dto.notes());

        StudyLog savedStudyLog = studyLogRepository.save(studyLog);
        log.info("✅ Estudo salvo com ID: {}", savedStudyLog.getId());

        try {
            // Passar os intervalos customizados do usuário
            int[] intervals = {
                    settings.getFirstRevisionInterval(),
                    settings.getSecondRevisionInterval(),
                    settings.getThirdRevisionInterval()
            };

            revisionService.createRevisionsForStudy(savedStudyLog, user, intervals);
            log.info("Revisões criadas automaticamente com intervalos: [{}, {}, {}]",
                    intervals[0], intervals[1], intervals[2]);
        } catch (Exception e) {
            log.error("Erro ao criar revisões para estudo {}: {}", savedStudyLog.getId(),
                    e.getMessage());
        }

        return new StudyLogResponseDto(savedStudyLog);
    }

    private UserSettings createDefaultSettings(User user) {
        UserSettings settings = UserSettings.builder()
                .user(user)
                .firstRevisionInterval(1)
                .secondRevisionInterval(7)
                .thirdRevisionInterval(14)
                .build();
        return userSettingsRepository.save(settings);
    }

    public List<StudyLogResponseDto> getAllStudies(Long userId) {
        log.info("Buscando todos os estudos do usuário: {}", userId);

        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return studyLogRepository.findByUserId(userId).stream()
                .map(StudyLogResponseDto::new)
                .collect(Collectors.toList());
    }

    public StudyLogResponseDto getStudyById(Long id, Long userId) {
        log.info("Buscando estudo: {} do usuário: {}", id, userId);

        StudyLog studyLog = studyLogRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Estudo não encontrado"));

        return new StudyLogResponseDto(studyLog);
    }

    @Transactional
    public StudyLogResponseDto updateStudy(Long id, StudyLogRegisterDto dto, Long userId) {
        log.info("Atualizando estudo: {} do usuário: {}", id, userId);

        StudyLog studyLog = studyLogRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Estudo não encontrado"));

        Discipline discipline = disciplineRepository.findById(dto.disciplineId())
                .orElseThrow(() -> new RuntimeException("Disciplina não encontrada"));

        studyLog.setDiscipline(discipline);
        studyLog.setStudyDate(dto.date());
        studyLog.setTheme(dto.topic());
        studyLog.setDurationMinutes(dto.getDurationMinutes());
        studyLog.setNotes(dto.notes());

        StudyLog updatedStudyLog = studyLogRepository.save(studyLog);
        log.info("Estudo {} atualizado com sucesso", id);

        return new StudyLogResponseDto(updatedStudyLog);
    }

    @Transactional
    public void deleteStudy(Long id, Long userId) {
        log.info("Deletando estudo: {} do usuário: {}", id, userId);

        StudyLog studyLog = studyLogRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Estudo não encontrado"));

        try {
            revisionService.deleteRevisionsByStudy(id);
            log.info("Revisões do estudo {} deletadas", id);
        } catch (Exception e) {
            log.error("Erro ao deletar revisões do estudo {}: {}", id, e.getMessage());
        }

        studyLogRepository.delete(studyLog);
        log.info("Estudo {} deletado com sucesso", id);
    }
}