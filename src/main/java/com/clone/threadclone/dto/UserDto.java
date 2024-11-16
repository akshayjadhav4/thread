package com.clone.threadclone.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class UserDto {
    private String displayName;

    private String username;

    private String email;

    private String profilePicture;

    private String bio;

    private LocalDateTime createdAt;
    private List<Long> followers;
    private List<Long> following;
}
