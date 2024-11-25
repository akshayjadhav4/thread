package com.clone.threadclone.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.clone.threadclone.model.Reply;
import com.clone.threadclone.request.EditReplyRequest;
import com.clone.threadclone.request.ReplyToThreadRequest;

public interface ReplyService {

    Reply replyToThread(ReplyToThreadRequest request);

    Page<Reply> getRepliesForThread(Long threadId, Pageable pageable);

    Page<Reply> getRepliesForParentReply(Long parentReplyId, Pageable pageable);

    void deleteReply(Long replyId);

    Reply editReply(Long replyId, EditReplyRequest request);
}
