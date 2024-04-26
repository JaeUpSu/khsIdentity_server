package com.example.khsIdentity.mapper;

import com.example.khsIdentity.dto.UserDTO;
import com.example.khsIdentity.domain.User;

import java.time.LocalDateTime;

public class Mapper {
    public UserDTO convertUserToDto(User user) {
        String name = user.getName();
        String userId = user.getUserId();
        String email = user.getEmail();
        LocalDateTime createdAt = user.getCreatedAt();

        return new UserDTO(name,userId,email,createdAt);
    }
}
