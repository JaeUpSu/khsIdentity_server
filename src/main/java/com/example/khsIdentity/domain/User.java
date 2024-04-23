package com.example.khsIdentity.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "이름을 작성해주세요!")
    @Column
    private String name;

    @NotBlank(message = "아이디를 작성해주세요!")
    @Column(nullable = false, unique = true)
    private String userId;

    @NotBlank
    @Size(min = 8, message = "비밀번호를 최소 8자 작성해주세요!")
    @Column
    private String password;

    @Email(message = "이메일 형식에 맞게 작성해주세요!")
    @Column(nullable = false, unique = true)
    private String email;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column
    private Boolean isManager;

    public User() {}

    public User(String name, String userId, String password, String email) {
        this.name = name;
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.isManager = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

}

