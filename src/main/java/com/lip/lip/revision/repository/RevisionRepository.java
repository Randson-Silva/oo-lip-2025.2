package com.lip.lip.revision.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lip.lip.revision.entity.Revision;

public interface RevisionRepository extends JpaRepository<Revision, Long>{
    
}
