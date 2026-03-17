package com.ilimitech.delivery.spring.users;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String phone;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "user_type")
    private String userType;

    private Boolean isVerified;
    private Boolean isActive;

    private LocalDateTime createdAt;

    public User() {}

    public User(Long id, String email, String phone, String fullName, String userType, Boolean isVerified, Boolean isActive, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.phone = phone;
        this.fullName = fullName;
        this.userType = userType;
        this.isVerified = isVerified;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String email;
        private String phone;
        private String fullName;
        private String userType;
        private Boolean isVerified;
        private Boolean isActive;
        private LocalDateTime createdAt;

        public Builder id(Long id){ this.id = id; return this; }
        public Builder email(String email){ this.email = email; return this; }
        public Builder phone(String phone){ this.phone = phone; return this; }
        public Builder fullName(String fullName){ this.fullName = fullName; return this; }
        public Builder userType(String userType){ this.userType = userType; return this; }
        public Builder isVerified(Boolean isVerified){ this.isVerified = isVerified; return this; }
        public Builder isActive(Boolean isActive){ this.isActive = isActive; return this; }
        public Builder createdAt(LocalDateTime createdAt){ this.createdAt = createdAt; return this; }
        public User build(){ return new User(id,email,phone,fullName,userType,isVerified,isActive,createdAt); }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }
    public Boolean getIsVerified() { return isVerified; }
    public void setIsVerified(Boolean isVerified) { this.isVerified = isVerified; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

