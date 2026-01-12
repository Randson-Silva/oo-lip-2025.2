package com.lip.lip.revision.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lip.lip.revision.dto.response.RevisionResponseDto;
import com.lip.lip.revision.dto.response.RevisionStatisticsDto;
import com.lip.lip.revision.entity.Revision;
import com.lip.lip.revision.repository.RevisionRepository;
import com.lip.lip.studylog.entity.StudyLog;
import com.lip.lip.user.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RevisionService {

    private final RevisionRepository revisionRepository;

    public List<RevisionResponseDto> getAllRevisions(Long userId) {
        return revisionRepository.findByUserId(userId)
                .stream()
                .map(RevisionResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<RevisionResponseDto> getOverdueRevisions(Long userId) {
        return revisionRepository.findOverdueRevisions(userId, LocalDate.now())
                .stream()
                .map(RevisionResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<RevisionResponseDto> getTodayRevisions(Long userId) {
        return revisionRepository.findTodayRevisions(userId, LocalDate.now())
                .stream()
                .map(RevisionResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<RevisionResponseDto> getPendingRevisions(Long userId) {
        return revisionRepository.findPendingRevisions(userId, LocalDate.now())
                .stream()
                .map(RevisionResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<RevisionResponseDto> createRevisionsForStudy(
            StudyLog studyLog,
            User user,
            int[] intervals) {

        LocalDate studyDate = studyLog.getStudyDate();
        List<Revision> created = new ArrayList<>();

        for (int i = 0; i < intervals.length; i++) {
            Revision revision = new Revision(
                    studyLog,
                    user,
                    studyDate.plusDays(intervals[i]),
                    i + 1);
            revisionRepository.save(revision);
            created.add(revision);
        }

        return created.stream().map(RevisionResponseDto::new).toList();
    }

    @Transactional
    public RevisionResponseDto toggleRevision(Long id, Long userId) {
        Revision revision = revisionRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Revisão não encontrada"));

        LocalDate today = LocalDate.now();

        if (!revision.isCompleted() && revision.getScheduledDate().isAfter(today)) {
            throw new RuntimeException(
                    "Esta revisão está agendada para o futuro (" +
                            revision.getScheduledDate().toString() +
                            ") e ainda não pode ser concluída.");
        }

        boolean newStatus = !revision.isCompleted();
        revision.setCompleted(newStatus);

        revision.setCompletedAt(newStatus ? today : null);

        revisionRepository.save(revision);
        return new RevisionResponseDto(revision);
    }

    @Transactional
    public RevisionResponseDto updateRevisionDate(
            Long revisionId,
            Long userId,
            LocalDate newDate) {
        Revision revision = revisionRepository.findByIdAndUserId(revisionId, userId)
                .orElseThrow(() -> new RuntimeException("Revisão não encontrada"));

        revision.setScheduledDate(newDate);
        revisionRepository.save(revision);

        return new RevisionResponseDto(revision);
    }

    @Transactional
    public void deleteRevision(Long id, Long userId) {
        Revision revision = revisionRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Revisão não encontrada"));

        revisionRepository.delete(revision);
    }

    public RevisionStatisticsDto getStatistics(Long userId) {
        LocalDate today = LocalDate.now();

        Long total = revisionRepository.countTotalRevisions(userId);
        Long completed = revisionRepository.countCompletedRevisionsByUserId(userId);
        Long pending = revisionRepository.countPendingRevisions(userId);
        Long overdue = revisionRepository.countOverdueRevisions(userId, today);

        var byRevision = new RevisionStatisticsDto.RevisionByTypeDto(
                revisionRepository.countRevision1(userId),
                revisionRepository.countRevision2(userId),
                revisionRepository.countRevision3(userId));

        return new RevisionStatisticsDto(total, completed, pending, overdue, byRevision);
    }

    @Transactional
    public void deleteRevisionsByStudy(Long studyLogId) {
        List<Revision> revisions = revisionRepository.findByStudyLogId(studyLogId);
        revisionRepository.deleteAll(revisions);
    }
}