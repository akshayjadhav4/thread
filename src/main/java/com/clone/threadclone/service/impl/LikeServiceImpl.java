package com.clone.threadclone.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.clone.threadclone.exceptions.ReplyNotFoundException;
import com.clone.threadclone.exceptions.ThreadNotFoundException;
import com.clone.threadclone.model.Like;
import com.clone.threadclone.model.Reply;
import com.clone.threadclone.model.User;
import com.clone.threadclone.repository.LikeRepository;
import com.clone.threadclone.repository.ReplyRepository;
import com.clone.threadclone.repository.ThreadRepository;
import com.clone.threadclone.service.LikeService;
import com.clone.threadclone.service.UserService;

import lombok.RequiredArgsConstructor;

import com.clone.threadclone.model.Thread;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final ThreadRepository threadRepository;
    private final ReplyRepository replyRepository;
    private final UserService userService;

    @Override
    public int likeThread(Long threadId) {
        if (threadId == null) {
            throw new IllegalArgumentException("Thread ID must not be null.");
        }

        User user = userService.getAuthenticatedUser();

        // Check if the user has already liked the thread
        if (likeRepository.existsByUserIdAndThreadId(user.getId(), threadId)) {
            throw new IllegalStateException("User has already liked this thread.");
        }

        // Fetch the thread
        Thread thread = threadRepository.findById(threadId)
                .orElseThrow(() -> new ThreadNotFoundException("Thread not found."));

        // Create and save the like
        Like like = new Like();
        like.setUser(user);
        like.setThread(thread);
        likeRepository.save(like);

        // Return the total count of likes for the thread
        return likeRepository.countByThreadId(threadId);
    }

    @Override
    public int unlikeThread(Long threadId) {
        if (threadId == null) {
            throw new IllegalArgumentException("Thread ID must not be null");
        }

        User user = userService.getAuthenticatedUser();

        // Check if the like exists
        Like like = likeRepository.findByUserIdAndThreadId(user.getId(), threadId)
                .orElseThrow(() -> new IllegalStateException("User has not liked this thread"));

        // Delete the like
        likeRepository.delete(like);

        // Return the updated total count of likes for the thread
        return likeRepository.countByThreadId(threadId);
    }

    @Override
    public int likeReply(Long replyId) {
        if (replyId == null) {
            throw new IllegalArgumentException("Reply ID must not be null.");
        }

        User user = userService.getAuthenticatedUser();

        // Check if the user has already liked the reply
        if (likeRepository.existsByUserIdAndReplyId(user.getId(), replyId)) {
            throw new IllegalStateException("User has already liked this Reply.");
        }

        // Fetch the reply
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new ReplyNotFoundException("Reply not found."));

        // Create and save the like
        Like like = new Like();
        like.setUser(user);
        like.setReply(reply);
        likeRepository.save(like);

        // Return the total count of likes for the reply
        return likeRepository.countByReplyId(replyId);
    }

    @Override
    public int unlikeReply(Long replyId) {
        if (replyId == null) {
            throw new IllegalArgumentException("Reply ID must not be null");
        }

        User user = userService.getAuthenticatedUser();

        // Check if the like exists
        Like like = likeRepository.findByUserIdAndReplyId(user.getId(), replyId)
                .orElseThrow(() -> new IllegalStateException("User has not liked this reply."));

        // Delete the like
        likeRepository.delete(like);

        // Return the updated total count of likes for the reply
        return likeRepository.countByThreadId(replyId);
    }

    @Override
    public Page<Like> getThreadLikes(Long threadId, Pageable pageable) {
        return likeRepository.findAllByThreadId(threadId, pageable);
    }

    @Override
    public Page<Like> getReplyLikes(Long replyId, Pageable pageable) {
        return likeRepository.findAllByReplyId(replyId, pageable);
    }

}
