package com.lip.lip.revision.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.lip.lip.revision.dto.request.RevisionUpdateRequest;
import com.lip.lip.revision.dto.response.RevisionResponseDto;
import com.lip.lip.revision.dto.response.RevisionStatisticsDto;
import com.lip.lip.revision.service.RevisionService;
import com.lip.lip.user.entity.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class RevisionController {

        private final RevisionService revisionService;

        @GetMapping
        public ResponseEntity<List<RevisionResponseDto>> getAll(Authentication auth) {
                User user = (User) auth.getPrincipal();
                return ResponseEntity.ok(revisionService.getAllRevisions(user.getId()));
        }

        @PatchMapping("/{id}")
        public ResponseEntity<RevisionResponseDto> updateDate(
                        @PathVariable Long id,
                        @RequestBody RevisionUpdateRequest request,
                        Authentication auth) {
                User user = (User) auth.getPrincipal();
                return ResponseEntity.ok(
                                revisionService.updateRevisionDate(id, user.getId(), request.dueDate()));
        }

        @PatchMapping("/{id}/toggle")
        public ResponseEntity<RevisionResponseDto> toggle(
                        @PathVariable Long id,
                        Authentication auth) {
                User user = (User) auth.getPrincipal();
                return ResponseEntity.ok(
                                revisionService.toggleRevision(id, user.getId()));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> delete(
                        @PathVariable Long id,
                        Authentication auth) {
                User user = (User) auth.getPrincipal();
                revisionService.deleteRevision(id, user.getId());
                return ResponseEntity.noContent().build();
        }

        @GetMapping("/statistics")
        public ResponseEntity<RevisionStatisticsDto> stats(Authentication auth) {
                User user = (User) auth.getPrincipal();
                return ResponseEntity.ok(revisionService.getStatistics(user.getId()));
        }
}
