package com.lip.lip.subject.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lip.lip.subject.dto.request.SubjectRegisterDto;
import com.lip.lip.subject.dto.request.SubjectUpdateDto;
import com.lip.lip.subject.dto.response.SubjectResponseDto;
import com.lip.lip.subject.entity.Subject;
import com.lip.lip.subject.repository.SubjectRepository;
import com.lip.lip.user.entity.User;
import com.lip.lip.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;

    @Transactional
    public SubjectResponseDto createSubject(SubjectRegisterDto dto, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (subjectRepository.existsByUserIdAndNameIgnoreCase(userId, dto.name())) {
            throw new RuntimeException("Subject with this name already exists for this user");
        }

        Subject newSubject = new Subject(dto, user);
        subjectRepository.save(newSubject);

        return new SubjectResponseDto(newSubject);
    }

    public List<SubjectResponseDto> getAllActiveSubjects(Long userId) {
        List<Subject> subjects = subjectRepository.findByUserIdAndActiveTrue(userId);

        return subjects.stream()
                .map(SubjectResponseDto::new)
                .collect(Collectors.toList());
    }

    public SubjectResponseDto getSubjectById(Long id, Long userId) {
        Subject subject = subjectRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        return new SubjectResponseDto(subject);
    }

    @Transactional
    public SubjectResponseDto updateSubject(Long id, SubjectUpdateDto dto, Long userId) {
        Subject subject = subjectRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        if (dto.name() != null && !dto.name().equals(subject.getName())) {
            if (subjectRepository.existsByUserIdAndNameIgnoreCase(userId, dto.name())) {
                throw new RuntimeException("Subject with this name already exists");
            }
            subject.setName(dto.name());
        }

        if (dto.color() != null) {
            subject.setColor(dto.color());
        }

        subjectRepository.save(subject);
        return new SubjectResponseDto(subject);
    }

    @Transactional
    public void inactivateSubject(Long id, Long userId) {
        Subject subject = subjectRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        // Soft delete
        subject.setActive(false);
        subjectRepository.save(subject);
    }

    @Transactional
    public void activateSubject(Long id, Long userId) {
        Subject subject = subjectRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        // Reverse soft delete
        subject.setActive(true);
        subjectRepository.save(subject);
    }

    @Transactional
    public void hardinactivateSubject(Long id, Long userId) {
        Subject subject = subjectRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        // Hard delete
        subjectRepository.delete(subject);
    }
}