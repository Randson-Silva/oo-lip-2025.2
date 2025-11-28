package com.lip.lip.user.entity;

import java.time.LocalDateTime;

import com.lip.lip.user.dto.request.UserRegisterDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="users")
@Getter
@Setter
@RequiredArgsConstructor
public class User {
    
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Size(max = 50, message = "The name must have a maximum of 50 characters.")
    @Pattern(
        regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ\\s'-]{2,50}$",
        message = "The name should contain only letters, spaces, apostrophes, or hyphens."
    )
    private String name;

    @Email(message = "Email is required")
    @Column(nullable = false, unique = true)
    @Pattern(
        regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
        message = "Invalid email format"
    )
    private String email;

    @Column(nullable = false)
    @Size(min = 3, message = "The password must have a minium of 3 characters.")
    private String password;

    @Column(name = "createdAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updatedAt", nullable = false)
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

    public User(UserRegisterDto userRegisterDto) {
        this.name = userRegisterDto.name();
        this.email = userRegisterDto.email();
        this.password = userRegisterDto.password();
    }

}
