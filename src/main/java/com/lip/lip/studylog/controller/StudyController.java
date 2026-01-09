package com.lip.lip.studylog.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lip.lip.revision.entity.Revision;
import com.lip.lip.revision.repository.RevisionRepository;
import com.lip.lip.studylog.entity.StudyLog;
import com.lip.lip.studylog.service.StudyLogService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/studies")
@RequiredArgsConstructor
public class StudyController {
    private final StudyLogService studyLogService;
    private final RevisionRepository revisionRepository;

    @PostMapping("/register")
    public ResponseEntity<StudyLog> registerStudy(@RequestBody StudyLog studyLog) {
        return ResponseEntity.ok(studyLogService.createStudy(studyLog));
    }

    @GetMapping("/revisions/{userId}")
    public ResponseEntity<Optional<Revision>> listRevisions(@PathVariable Long userId) {
        return ResponseEntity.ok(revisionRepository.findById(userId));
    }
}
