package com.example.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String name;
    private String picture;
    
    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean emailVerified = false;
    
    private String verificationToken;
    
    private LocalDateTime verificationTokenExpiresAt;
    
    private LocalDateTime createdAt;

    public User() {
        this.createdAt = LocalDateTime.now();
    }

    public User(String email, String name, String picture) {
        this.email = email;
        this.name = name;
        this.picture = picture;
        this.emailVerified = false;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
    
    public Boolean getEmailVerified() {
        return emailVerified;
    }
    
    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }
    
    public String getVerificationToken() {
        return verificationToken;
    }
    
    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }
    
    public LocalDateTime getVerificationTokenExpiresAt() {
        return verificationTokenExpiresAt;
    }
    
    public void setVerificationTokenExpiresAt(LocalDateTime verificationTokenExpiresAt) {
        this.verificationTokenExpiresAt = verificationTokenExpiresAt;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}