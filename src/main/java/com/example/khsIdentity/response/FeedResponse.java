package com.example.khsIdentity.response;

import com.example.khsIdentity.domain.Content;
import com.example.khsIdentity.domain.Feed;
import com.example.khsIdentity.dto.UserDTO;
import com.example.khsIdentity.mapper.Mapper;

import java.time.LocalDateTime;
import java.util.List;

public class FeedResponse {
    private Long id;
    private String title;
    private LocalDateTime createdAt;

    private List<Content> contents;

    private UserDTO user;

    private boolean isPrivate;

    public FeedResponse(Feed feed) {
        this.id = feed.getId();
        this.title = feed.getTitle();
        this.createdAt = feed.getCreatedAt();
        this.contents = feed.getContents();
        this.isPrivate = feed.getIsPrivate();
        this.user = new Mapper().convertUserToDto(feed.getUser());
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
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

    public List<Content> getContents() {
        return contents;
    }

    public void setContents(List<Content> contents) {
        this.contents = contents;
    }

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }
}
