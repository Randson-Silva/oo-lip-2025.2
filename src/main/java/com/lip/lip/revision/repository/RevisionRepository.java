package com.lip.lip.revision.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lip.lip.revision.entity.Revision;

public interface RevisionRepository extends JpaRepository<Revision, Long> {

    @Query("SELECT COUNT(r) FROM Revision r WHERE r.user.id = :userId AND r.completed = true")
    Long countCompletedRevisionsByUserId(@Param("userId") Long userId);

    List<Revision> findByUserId(Long userId);

    Optional<Revision> findByIdAndUserId(Long id, Long userId);

    List<Revision> findByStudyLogId(Long studyLogId);

    @Query("SELECT r FROM Revision r WHERE r.user.id = :userId AND r.completed = false AND r.scheduledDate < :today")
    List<Revision> findOverdueRevisions(@Param("userId") Long userId, @Param("today") LocalDate today);

    @Query("SELECT r FROM Revision r WHERE r.user.id = :userId AND r.completed = false AND r.scheduledDate = :today")
    List<Revision> findTodayRevisions(@Param("userId") Long userId, @Param("today") LocalDate today);

    @Query("SELECT r FROM Revision r WHERE r.user.id = :userId AND r.completed = false AND r.scheduledDate <= :today")
    List<Revision> findPendingRevisions(@Param("userId") Long userId, @Param("today") LocalDate today);

    @Query("SELECT COUNT(r) FROM Revision r WHERE r.user.id = :userId")
    Long countTotalRevisions(@Param("userId") Long userId);

    @Query("SELECT COUNT(r) FROM Revision r WHERE r.user.id = :userId AND r.completed = false")
    Long countPendingRevisions(@Param("userId") Long userId);

    @Query("SELECT COUNT(r) FROM Revision r WHERE r.user.id = :userId AND r.completed = false AND r.scheduledDate < :today")
    Long countOverdueRevisions(@Param("userId") Long userId, @Param("today") LocalDate today);

    @Query("SELECT COUNT(r) FROM Revision r WHERE r.user.id = :userId AND r.revisionNumber = 1")
    Long countRevision1(@Param("userId") Long userId);

    @Query("SELECT COUNT(r) FROM Revision r WHERE r.user.id = :userId AND r.revisionNumber = 2")
    Long countRevision2(@Param("userId") Long userId);

    @Query("SELECT COUNT(r) FROM Revision r WHERE r.user.id = :userId AND r.revisionNumber = 3")
    Long countRevision3(@Param("userId") Long userId);
}