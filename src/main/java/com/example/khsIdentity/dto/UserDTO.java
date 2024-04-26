package com.example.khsIdentity.dto;

import java.time.LocalDateTime;

public class UserDTO {
    private String name;
    private String userId;
    private String email;
    private LocalDateTime createdAt;

    public UserDTO(String name, String userId, String email, LocalDateTime createdAt) {
        this.name = name;
        this.userId = userId;
        this.email = email;
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
