package com.lip.lip.revision.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lip.lip.revision.entity.Revision;

public interface RevisionRepository extends JpaRepository<Revision, Long> {

    @Query("SELECT COUNT(r) FROM Revision r WHERE r.user.id = :userId AND r.completed = true")
    Long countCompletedRevisionsByUserId(@Param("userId") Long userId);

    // Buscar todas as revisões de um usuário
    List<Revision> findByUserId(Long userId);

    // Buscar revisão específica de um usuário
    Optional<Revision> findByIdAndUserId(Long id, Long userId);
}