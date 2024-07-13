package com.upwork.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "\"url\"")
public class Url {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "original_url", nullable = false)
    private String originalUrl;

    @Column(name = "short_url", nullable = false, unique = true)
    private String shortUrl;

    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;

    @Column(name = "last_access_time", nullable = false)
    private LocalDateTime lastAccessTime;

    public Url() {
        // Initialize default values
        this.lastAccessTime = LocalDateTime.now();
        this.expiryDate = LocalDateTime.now().plusDays(30); // Default expiry date
    }

    // Getters and setters

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryDate);
    }

    public void updateLastAccessTime() {
        this.lastAccessTime = LocalDateTime.now();
    }

}
