package com.victor.BookFlix.entity;


import com.victor.BookFlix.role.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username; // login username

    @Column(nullable = false, unique = true)
    private String email; // unique email

    @Column(nullable = false)
    private String password; // encrypted with BCrypt


    @Column(nullable = false)
    private String role; // USER or ADMIN

    private boolean active = true; // account status

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
