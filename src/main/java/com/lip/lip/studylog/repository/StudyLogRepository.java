package com.lip.lip.studylog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lip.lip.studylog.entity.StudyLog;

public interface StudyLogRepository extends JpaRepository<StudyLog, Long>{
    
}
