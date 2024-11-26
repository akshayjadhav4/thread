package com.clone.threadclone.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.clone.threadclone.model.Like;

public interface LikeService {

    int likeThread(Long threadId);

    int unlikeThread(Long threadId);

    int likeReply(Long replyId);

    int unlikeReply(Long replyId);

    Page<Like> getThreadLikes(Long threadId, Pageable pageable);

    Page<Like> getReplyLikes(Long replyId, Pageable pageable);
}
