package com.lip.lip.subject.entity;

import java.time.LocalDateTime;

import com.lip.lip.subject.dto.request.SubjectRegisterDto;
import com.lip.lip.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "subjects")
@Getter
@Setter
@RequiredArgsConstructor
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    @Size(max = 50, message = "The name must have a maximum of 50 characters.")
    @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ\\s'-]{2,50}$", message = "The name should contain only letters, spaces, apostrophes, or hyphens.")
    private String name;

    @Column(nullable = true)
    private String color;

    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Subject(SubjectRegisterDto subjectRegisterDto, User user) {
        this.name = subjectRegisterDto.name();
        this.user = user;
        this.color = subjectRegisterDto.color();
        this.active = true; // always start active
    }
}