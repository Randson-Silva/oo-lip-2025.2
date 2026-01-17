package com.lip.lip.studylog.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lip.lip.disicipline.dtos.response.DisciplinePerformanceDto;
import com.lip.lip.studylog.entity.StudyLog;

public interface StudyLogRepository extends JpaRepository<StudyLog, Long> {

    @Query("SELECT COUNT(s) FROM StudyLog s WHERE s.user.id = :userId")
    Long countTotalStudiesByUserId(@Param("userId") Long userId);

    @Query("SELECT new com.lip.lip.disicipline.dtos.response.DisciplinePerformanceDto(d.name, COUNT(s), 0L) " +
            "FROM StudyLog s JOIN s.discipline d " +
            "WHERE s.user.id = :userId GROUP BY d.name")
    List<DisciplinePerformanceDto> getStudiesPerDiscipline(@Param("userId") Long userId);

    // Buscar todos os estudos de um usuário
    List<StudyLog> findByUserId(Long userId);

    // Buscar estudo específico de um usuário
    Optional<StudyLog> findByIdAndUserId(Long id, Long userId);

    List<StudyLog> findByUserIdOrderByStudyDateDesc(Long userId);
}