package com.lip.lip.disicipline.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lip.lip.disicipline.dtos.request.DisciplineRegisterDto;
import com.lip.lip.disicipline.dtos.response.DisciplineResponseDto;
import com.lip.lip.disicipline.entity.Discipline;
import com.lip.lip.disicipline.repository.DisiciplineRepository;
import com.lip.lip.user.entity.User;
import com.lip.lip.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DisciplineService {

        private final DisiciplineRepository disciplineRepository;
        private final UserRepository userRepository;

        @Transactional
        public DisciplineResponseDto createDiscipline(DisciplineRegisterDto dto, Long userId) {
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                Discipline discipline = new Discipline(dto, user);
                disciplineRepository.save(discipline);

                return new DisciplineResponseDto(
                                discipline.getId(),
                                discipline.getName(),
                                discipline.getUser().getId(),
                                discipline.getColor());
        }

        public List<DisciplineResponseDto> getAllDisciplines(Long userId) {
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                return disciplineRepository.findByUserAndActiveTrue(user).stream()
                                .map(d -> new DisciplineResponseDto(
                                                d.getId(),
                                                d.getName(),
                                                d.getUser().getId(),
                                                d.getColor()))
                                .collect(Collectors.toList());
        }

        @Transactional
        public DisciplineResponseDto updateDiscipline(Long id, DisciplineRegisterDto dto, Long userId) {
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                Discipline discipline = disciplineRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Discipline not found"));

                if (!discipline.getUser().getId().equals(user.getId())) {
                        throw new RuntimeException("Unauthorized");
                }

                discipline.setName(dto.name());
                discipline.setColor(dto.color());
                disciplineRepository.save(discipline);

                return new DisciplineResponseDto(
                                discipline.getId(),
                                discipline.getName(),
                                discipline.getUser().getId(),
                                discipline.getColor()
                        );
        }

        @Transactional
        public void deleteDiscipline(Long id, Long userId) {
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                Discipline discipline = disciplineRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Discipline not found"));

                if (!discipline.getUser().getId().equals(user.getId())) {
                        throw new RuntimeException("Unauthorized");
                }

                discipline.setActive(false);
                disciplineRepository.save(discipline);
        }
}