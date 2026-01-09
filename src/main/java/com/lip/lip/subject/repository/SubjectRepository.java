package com.lip.lip.subject.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lip.lip.subject.entity.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

    List<Subject> findByUserIdAndActiveTrue(Long userId);

    boolean existsByUserIdAndNameIgnoreCase(Long userId, String name);

    Optional<Subject> findByIdAndUserId(Long id, Long userId);

    List<Subject> findByUserId(Long userId);
}