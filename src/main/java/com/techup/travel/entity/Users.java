package com.techup.travel.entity;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.*;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="users")

public class Users {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name="password", nullable = false)
    private String password;

    @Column(name="display_name", length = 100)
    private String displayName;

    @Builder.Default
    @Column(name="created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @PrePersist
    private void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }
}
