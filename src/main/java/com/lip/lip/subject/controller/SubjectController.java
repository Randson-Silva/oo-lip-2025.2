package com.lip.lip.subject.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lip.lip.subject.dto.request.SubjectRegisterDto;
import com.lip.lip.subject.dto.request.SubjectUpdateDto;
import com.lip.lip.subject.dto.response.SubjectResponseDto;
import com.lip.lip.subject.service.SubjectService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/subject")
@Tag(name = "Subject Controller", description = "Endpoints for managing study subjects")
public class SubjectController {

        private final SubjectService subjectService;

        public SubjectController(SubjectService subjectService) {
                this.subjectService = subjectService;
        }

        @Operation(description = "Register a new subject into system")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Subject registered successfully"),
                        @ApiResponse(responseCode = "400", description = "Invalid registration data (e.g., subject name already exists for this user)")
        })
        @PostMapping
        public ResponseEntity<SubjectResponseDto> createSubject(
                        @RequestBody @Valid SubjectRegisterDto dto,
                        @RequestHeader("userId") Long userId) { // ! Temporary. Then use JWT/Security

                SubjectResponseDto subjectResponseDto = subjectService.createSubject(dto, userId);
                return ResponseEntity.status(HttpStatus.CREATED).body(subjectResponseDto);
        }

        @Operation(description = "Get all active subjects for the logged user")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Successfully retrieved all active subjects"),
                        @ApiResponse(responseCode = "404", description = "User not found")
        })
        @GetMapping
        public ResponseEntity<List<SubjectResponseDto>> getAllActiveSubjects(
                        @RequestHeader("userId") Long userId) {

                return ResponseEntity.ok(subjectService.getAllActiveSubjects(userId));
        }

        @Operation(description = "Get subject by ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Successfully retrieved the subject"),
                        @ApiResponse(responseCode = "404", description = "Subject not found"),
                        @ApiResponse(responseCode = "403", description = "Subject does not belong to this user")
        })
        @GetMapping("/{id}")
        public ResponseEntity<SubjectResponseDto> getSubjectById(
                        @PathVariable Long id,
                        @RequestHeader("userId") Long userId) {

                return ResponseEntity.ok(subjectService.getSubjectById(id, userId));
        }

        @Operation(description = "Update a subject's name and/or color")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Subject updated successfully"),
                        @ApiResponse(responseCode = "404", description = "Subject not found"),
                        @ApiResponse(responseCode = "400", description = "Invalid data provided or subject name already exists")
        })
        @PatchMapping("/{id}")
        public ResponseEntity<SubjectResponseDto> updateSubject(
                        @PathVariable Long id,
                        @RequestBody @Valid SubjectUpdateDto dto,
                        @RequestHeader("userId") Long userId) {

                return ResponseEntity.ok(subjectService.updateSubject(id, dto, userId));
        }

        @Operation(summary = "Deactivate a subject (soft delete)", description = "Mark subject as inactive. It won't be deleted from database.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Subject deactivated successfully"),
                        @ApiResponse(responseCode = "404", description = "Subject not found")
        })
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> inactivateSubject(
                        @PathVariable Long id,
                        @RequestHeader("userId") Long userId) {

                subjectService.inactivateSubject(id, userId);
                return ResponseEntity.noContent().build();
        }

        @Operation(summary = "Activate a subject (reverts soft delete)", description = "Mark subject as active.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Subject activated successfully"),
                        @ApiResponse(responseCode = "404", description = "Subject not found")
        })
        @PostMapping("/{id}")
        public ResponseEntity<Void> activateSubject(
                        @PathVariable Long id,
                        @RequestHeader("userId") Long userId) {

                subjectService.activateSubject(id, userId);
                return ResponseEntity.noContent().build();
        }

        @Operation(summary = "Permanently delete a subject (hard delete)", description = "Completely remove subject from database. Use with caution!")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Subject permanently deleted"),
                        @ApiResponse(responseCode = "404", description = "Subject not found"),
                        @ApiResponse(responseCode = "409", description = "Cannot delete - subject has related studies")
        })
        @DeleteMapping("/{id}/permanent")
        public ResponseEntity<Void> hardinactivateSubject(
                        @PathVariable Long id,
                        @RequestHeader("userId") Long userId) {

                subjectService.hardinactivateSubject(id, userId);
                return ResponseEntity.noContent().build();
        }
}