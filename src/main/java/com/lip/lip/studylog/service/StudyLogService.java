package com.lip.lip.studylog.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lip.lip.revision.entity.Revision;
import com.lip.lip.revision.repository.RevisionRepository;
import com.lip.lip.studylog.entity.StudyLog;
import com.lip.lip.studylog.repository.StudyLogRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudyLogService {

    private final StudyLogRepository studyLogRepository;
    private final RevisionRepository revisionRepository;

    @Transactional
    public StudyLog createStudyAndScheduleRevisions(StudyLog studyLog) {
        StudyLog savedLog = studyLogRepository.save(studyLog);

        List<Integer> revisionDays = List.of(1, 7, 14);

        revisionDays.forEach(daysToAdd -> {
            Revision revision = new Revision();
            revision.setScheduledDate(savedLog.getStudyDate().plusDays(daysToAdd));
            revision.setStudyLog(savedLog);
            revision.setUser(savedLog.getUser());
            revision.setCompleted(false);
            
            revisionRepository.save(revision);
        });

        return savedLog;
    }
}
