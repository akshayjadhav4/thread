package com.clone.threadclone.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ReplyDto {

    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private UserDto user;
    private int likeCount;
}
