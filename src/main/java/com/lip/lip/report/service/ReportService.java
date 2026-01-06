package com.lip.lip.report.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lip.lip.revision.repository.RevisionRepository;
import com.lip.lip.studylog.repository.StudyLogRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final StudyLogRepository studyLogRepository;
    private final RevisionRepository revisionRepository;

    public Map<String, Object> getUserGeneralStats(Long userId) {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalStudies", studyLogRepository.countTotalStudiesByUserId(userId));
        stats.put("completedRevisions", revisionRepository.countCompletedRevisionsByUserId(userId));
        
        stats.put("performanceByDiscipline", studyLogRepository.getStudiesPerDiscipline(userId));
        
        return stats;
    }
}
