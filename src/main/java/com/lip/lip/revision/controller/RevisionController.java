package com.lip.lip.revision.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lip.lip.revision.dto.response.RevisionResponseDto;
import com.lip.lip.revision.service.RevisionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Tag(name = "Revision Controller", description = "Endpoints for managing study revisions")
public class RevisionController {

    private final RevisionService revisionService;

    @Operation(description = "Get all revisions for a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all revisions"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping
    public ResponseEntity<List<RevisionResponseDto>> getAllRevisions(
            @RequestHeader("userId") Long userId) {

        return ResponseEntity.ok(revisionService.getAllRevisions(userId));
    }

    @Operation(description = "Toggle revision completion status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Revision status toggled successfully"),
            @ApiResponse(responseCode = "404", description = "Revision not found")
    })
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<RevisionResponseDto> toggleRevision(
            @PathVariable Long id,
            @RequestHeader("userId") Long userId) {

        return ResponseEntity.ok(revisionService.toggleRevision(id, userId));
    }

    @Operation(description = "Delete a revision")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Revision deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Revision not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRevision(
            @PathVariable Long id,
            @RequestHeader("userId") Long userId) {

        revisionService.deleteRevision(id, userId);
        return ResponseEntity.noContent().build();
    }
}