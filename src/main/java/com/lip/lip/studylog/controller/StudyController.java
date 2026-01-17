package com.lip.lip.studylog.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lip.lip.studylog.dtos.request.StudyLogRegisterDto;
import com.lip.lip.studylog.dtos.response.StudyLogResponseDto;
import com.lip.lip.studylog.service.StudyLogService;
import com.lip.lip.user.entity.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/studies")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Study Controller", description = "Endpoints para gerenciar registros de estudo")
public class StudyController {

        private final StudyLogService studyLogService;

        @Operation(description = "Registrar um novo estudo e criar revisões automaticamente")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Estudo registrado com sucesso"),
                        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                        @ApiResponse(responseCode = "401", description = "Não autorizado")
        })
        @PostMapping
        public ResponseEntity<StudyLogResponseDto> registerStudy(
                        @RequestBody @Valid StudyLogRegisterDto dto,
                        Authentication authentication) {

                log.info("Registrando novo estudo");

                User user = (User) authentication.getPrincipal();
                StudyLogResponseDto response = studyLogService.createStudy(dto, user.getId());

                return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        @Operation(description = "Buscar todos os estudos do usuário logado")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Estudos encontrados com sucesso"),
                        @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
                        @ApiResponse(responseCode = "401", description = "Não autorizado")
        })
        @GetMapping
        public ResponseEntity<List<StudyLogResponseDto>> getAllStudies(
                        Authentication authentication) {

                log.info("Buscando todos os estudos do usuário");

                User user = (User) authentication.getPrincipal();
                return ResponseEntity.ok(studyLogService.getAllStudies(user.getId()));
        }

        @Operation(description = "Buscar estudo específico por ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Estudo encontrado"),
                        @ApiResponse(responseCode = "404", description = "Estudo não encontrado"),
                        @ApiResponse(responseCode = "401", description = "Não autorizado")
        })
        @GetMapping("/{id}")
        public ResponseEntity<StudyLogResponseDto> getStudyById(
                        @PathVariable Long id,
                        Authentication authentication) {

                log.info("Buscando estudo com ID: {}", id);

                User user = (User) authentication.getPrincipal();
                return ResponseEntity.ok(studyLogService.getStudyById(id, user.getId()));
        }

        @Operation(description = "Atualizar um estudo existente")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Estudo atualizado com sucesso"),
                        @ApiResponse(responseCode = "404", description = "Estudo não encontrado"),
                        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                        @ApiResponse(responseCode = "401", description = "Não autorizado")
        })
        @PutMapping("/{id}")
        public ResponseEntity<StudyLogResponseDto> updateStudy(
                        @PathVariable Long id,
                        @RequestBody @Valid StudyLogRegisterDto dto,
                        Authentication authentication) {

                log.info("Atualizando estudo com ID: {}", id);

                User user = (User) authentication.getPrincipal();
                return ResponseEntity.ok(studyLogService.updateStudy(id, dto, user.getId()));
        }

        @Operation(description = "Deletar um estudo e suas revisões associadas")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Estudo deletado com sucesso"),
                        @ApiResponse(responseCode = "404", description = "Estudo não encontrado"),
                        @ApiResponse(responseCode = "401", description = "Não autorizado")
        })
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteStudy(
                        @PathVariable Long id,
                        Authentication authentication) {

                log.info("Deletando estudo com ID: {}", id);

                User user = (User) authentication.getPrincipal();
                studyLogService.deleteStudy(id, user.getId());

                return ResponseEntity.noContent().build();
        }
}