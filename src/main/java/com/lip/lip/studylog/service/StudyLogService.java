package com.lip.lip.studylog.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lip.lip.revision.entity.Revision;
import com.lip.lip.revision.repository.RevisionRepository;
import com.lip.lip.studylog.entity.StudyLog;
import com.lip.lip.studylog.repository.StudyLogRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudyLogService {

    private final StudyLogRepository studyLogRepository;
    private final RevisionRepository revisionRepository;

    @Transactional
    public StudyLog createStudy(StudyLog log) {
        log.setStudyDate(LocalDate.now());
        StudyLog savedLog = studyLogRepository.save(log);

        List<Integer> revisionGaps = List.of(1, 7, 14);

        for (Integer days : revisionGaps) {
            Revision revision = new Revision();
            revision.setScheduledDate(savedLog.getStudyDate().plusDays(days));
            revision.setStudyLog(savedLog);
            revision.setUser(savedLog.getUser());
            revision.setCompleted(false);
            
            revisionRepository.save(revision);
        }

        return savedLog;
    }
}