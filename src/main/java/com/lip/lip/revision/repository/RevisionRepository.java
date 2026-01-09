package com.lip.lip.revision.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lip.lip.revision.entity.Revision;

public interface RevisionRepository extends JpaRepository<Revision, Long>{
    @Query("SELECT COUNT(r) FROM Revision r WHERE r.user.id = :userId AND r.completed = true")
    Long countCompletedRevisionsByUserId(@Param("userId") Long userId);
}
