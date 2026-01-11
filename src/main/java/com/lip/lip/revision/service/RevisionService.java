package com.lip.lip.revision.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lip.lip.revision.dto.response.RevisionResponseDto;
import com.lip.lip.revision.entity.Revision;
import com.lip.lip.revision.repository.RevisionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RevisionService {

    private final RevisionRepository revisionRepository;

    public List<RevisionResponseDto> getAllRevisions(Long userId) {
        List<Revision> revisions = revisionRepository.findByUserId(userId);

        return revisions.stream()
                .map(RevisionResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public RevisionResponseDto toggleRevision(Long id, Long userId) {
        Revision revision = revisionRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Revision not found"));

        // Toggle completed status
        revision.setCompleted(!revision.isCompleted());
        revisionRepository.save(revision);

        return new RevisionResponseDto(revision);
    }

    @Transactional
    public void deleteRevision(Long id, Long userId) {
        Revision revision = revisionRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Revision not found"));

        revisionRepository.delete(revision);
    }
}