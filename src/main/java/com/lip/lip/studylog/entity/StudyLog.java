package com.lip.lip.studylog.entity;

import java.time.LocalDate;

import com.lip.lip.disicipline.entity.Discipline;
import com.lip.lip.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="study_log")
@Getter
@Setter
@RequiredArgsConstructor
public class StudyLog {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String theme;

    @Column(nullable = false)
    private Integer durationMinutes;

    @Column(nullable = false)
    private LocalDate studyDate;

    @ManyToOne
    @JoinColumn(name="discipline_id", nullable = false)
    private Discipline discipline;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;
}
