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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "revision")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Revision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate scheduledDate;

    @Column(nullable = false)
    private boolean completed = false;

    @Column(nullable = false)
    private Integer revisionNumber;

    @Column
    private LocalDate completedAt;

    @ManyToOne
    @JoinColumn(name = "study_log_id", nullable = false)
    private StudyLog studyLog;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Revision(StudyLog studyLog, User user, LocalDate scheduledDate, Integer revisionNumber) {
        this.studyLog = studyLog;
        this.user = user;
        this.scheduledDate = scheduledDate;
        this.revisionNumber = revisionNumber;
        this.completed = false;
    }
}