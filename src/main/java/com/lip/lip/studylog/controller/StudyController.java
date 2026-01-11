package com.lip.lip.studylog.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lip.lip.studylog.dtos.request.StudyLogRegisterDto;
import com.lip.lip.studylog.dtos.response.StudyLogResponseDto;
import com.lip.lip.studylog.service.StudyLogService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/studies")
@RequiredArgsConstructor
@Tag(name = "Study Controller", description = "Endpoints for managing study logs")
public class StudyController {
    
    private final StudyLogService studyLogService;

    @Operation(description = "Register a new study")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Study registered successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid data")
    })
    @PostMapping
    public ResponseEntity<StudyLogResponseDto> registerStudy(
            @RequestBody @Valid StudyLogRegisterDto dto,
            @RequestHeader("userId") Long userId) {
        
                long id = 1;

        StudyLogResponseDto response = studyLogService.createStudy(dto, id);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(description = "Get all studies for a user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved studies"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping
    public ResponseEntity<List<StudyLogResponseDto>> getAllStudies(
            @RequestHeader("userId") Long userId) {
        
        return ResponseEntity.ok(studyLogService.getAllStudies(userId));
    }

    @Operation(description = "Get study by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved study"),
        @ApiResponse(responseCode = "404", description = "Study not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<StudyLogResponseDto> getStudyById(
            @PathVariable Long id,
            @RequestHeader("userId") Long userId) {
        
        return ResponseEntity.ok(studyLogService.getStudyById(id, userId));
    }

    @Operation(description = "Update a study")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Study updated successfully"),
        @ApiResponse(responseCode = "404", description = "Study not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<StudyLogResponseDto> updateStudy(
            @PathVariable Long id,
            @RequestBody @Valid StudyLogRegisterDto dto,
            @RequestHeader("userId") Long userId) {
        
        return ResponseEntity.ok(studyLogService.updateStudy(id, dto, userId));
    }

    @Operation(description = "Delete a study")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Study deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Study not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudy(
            @PathVariable Long id,
            @RequestHeader("userId") Long userId) {
        
        studyLogService.deleteStudy(id, userId);
        return ResponseEntity.noContent().build();
    }
}