package com.example.khsIdentity.response;

import com.example.khsIdentity.domain.Feed;

import java.time.LocalDateTime;

public class FeedSimpleResponse {
    private Long id;
    private String title;
    private LocalDateTime createdAt;

    private String username;

    public FeedSimpleResponse(Feed feed) {
        this.id = feed.getId();
        this.title = feed.getTitle();
        this.createdAt = feed.getCreatedAt();
        this.username = feed.getUser().getName();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
