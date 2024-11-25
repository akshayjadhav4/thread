package com.clone.threadclone.request;

import lombok.Data;

@Data
public class ReplyToThreadRequest {

    private String content;

    private Long threadId;

    private Long parentReplyId;
}
