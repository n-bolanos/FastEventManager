package com.fem.authentication.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;

    @Column(nullable=false, unique = false)
    private String name; 

    @Column(nullable=false, unique = true)
    private String username; 

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable=false)
    private String passwordHash;
}
