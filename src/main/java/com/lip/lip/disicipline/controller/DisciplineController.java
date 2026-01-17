package com.lip.lip.disicipline.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.lip.lip.disicipline.dtos.request.DisciplineRegisterDto;
import com.lip.lip.disicipline.dtos.response.DisciplineResponseDto;
import com.lip.lip.disicipline.service.DisciplineService;
import com.lip.lip.user.entity.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/disciplines")
@RequiredArgsConstructor
@Tag(name = "Discipline Controller", description = "Endpoints for managing disciplines")
public class DisciplineController {

        private final DisciplineService disciplineService;

        @Operation(description = "Create a new discipline")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Discipline created successfully"),
                        @ApiResponse(responseCode = "400", description = "Invalid data")
        })
        @PostMapping
        public ResponseEntity<DisciplineResponseDto> createDiscipline(
                        @RequestBody @Valid DisciplineRegisterDto dto,
                        Authentication authentication) {

                User user = (User) authentication.getPrincipal();
                DisciplineResponseDto response = disciplineService.createDiscipline(dto, user.getId());
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        @Operation(description = "Get all active disciplines for a user")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Successfully retrieved disciplines"),
                        @ApiResponse(responseCode = "404", description = "User not found")
        })
        @GetMapping
        public ResponseEntity<List<DisciplineResponseDto>> getAllDisciplines(
                        Authentication authentication) {

                User user = (User) authentication.getPrincipal();
                return ResponseEntity.ok(disciplineService.getAllDisciplines(user.getId()));
        }

        @Operation(description = "Update a discipline")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Discipline updated successfully"),
                        @ApiResponse(responseCode = "404", description = "Discipline not found")
        })
        @PutMapping("/{id}")
        public ResponseEntity<DisciplineResponseDto> updateDiscipline(
                        @PathVariable Long id,
                        @RequestBody @Valid DisciplineRegisterDto dto,
                        Authentication authentication) {

                User user = (User) authentication.getPrincipal();
                return ResponseEntity.ok(disciplineService.updateDiscipline(id, dto, user.getId()));
        }

        @Operation(description = "Delete a discipline")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Discipline deleted successfully"),
                        @ApiResponse(responseCode = "404", description = "Discipline not found")
        })
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteDiscipline(
                        @PathVariable Long id,
                        Authentication authentication) {

                User user = (User) authentication.getPrincipal();
                disciplineService.deleteDiscipline(id, user.getId());
                return ResponseEntity.noContent().build();
        }
}
