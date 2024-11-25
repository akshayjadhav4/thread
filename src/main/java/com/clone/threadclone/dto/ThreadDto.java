package com.clone.threadclone.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class ThreadDto {

    private Long id;

    private String content;

    private LocalDateTime createdAt;

    private UserDto user;

    private List<MediaDto> medias;

}
