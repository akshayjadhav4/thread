package com.clone.threadclone.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LikeDto {

    private Long id;

    private LocalDateTime createdAt;

    private UserDto user;
}
