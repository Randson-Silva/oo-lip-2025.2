package com.lip.lip.disicipline.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lip.lip.disicipline.entity.Discipline;
import com.lip.lip.user.entity.User;

public interface DisiciplineRepository extends JpaRepository<Discipline, Long> {
    
    List<Discipline> findByUserAndActiveTrue(User user);
    
    List<Discipline> findByUser(User user);
}