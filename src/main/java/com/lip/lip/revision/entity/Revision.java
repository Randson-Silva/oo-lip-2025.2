package com.lip.lip.revision.entity;

import java.time.LocalDate;

import com.lip.lip.studylog.entity.StudyLog;
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
@Table(name="revision")
@Getter
@Setter
@RequiredArgsConstructor
public class Revision {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate scheduledDate; 

    @Column(nullable = false)
    private boolean completed = false;

    @ManyToOne
    @JoinColumn(name="study_log_id", nullable = false)
    private StudyLog studyLog;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;
}
