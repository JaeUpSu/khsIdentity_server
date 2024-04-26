package com.example.khsIdentity.response;

import com.example.khsIdentity.domain.Feed;
import com.example.khsIdentity.dto.UserDTO;
import com.example.khsIdentity.mapper.Mapper;

import java.time.LocalDateTime;

public class FeedResponse {
    private Long id;
    private String title;
    private LocalDateTime createdAt;
    private UserDTO user;

    public FeedResponse(Feed feed) {
        this.id = feed.getId();
        this.title = feed.getTitle();
        this.createdAt = feed.getCreatedAt();
        this.user = new Mapper().convertUserToDto(feed.getUser());
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
