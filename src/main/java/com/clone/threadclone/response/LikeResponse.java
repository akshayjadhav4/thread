package com.clone.threadclone.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LikeResponse {

    private int likeCount; // Total likes
    private Long threadId; // Optional, only relevant for threads
    private Long replyId; // Optional, only relevant for replies
}
