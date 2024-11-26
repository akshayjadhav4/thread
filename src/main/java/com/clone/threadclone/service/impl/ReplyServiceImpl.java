package com.clone.threadclone.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.clone.threadclone.exceptions.ReplyNotFoundException;
import com.clone.threadclone.exceptions.ThreadNotFoundException;
import com.clone.threadclone.model.Reply;
import com.clone.threadclone.model.User;
import com.clone.threadclone.model.Thread;
import com.clone.threadclone.repository.ReplyRepository;
import com.clone.threadclone.repository.ThreadRepository;
import com.clone.threadclone.request.EditReplyRequest;
import com.clone.threadclone.request.ReplyToThreadRequest;
import com.clone.threadclone.service.ReplyService;
import com.clone.threadclone.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {

    private final ReplyRepository replyRepository;
    private final ThreadRepository threadRepository;
    private final UserService userService;

    @Override
    public Reply replyToThread(ReplyToThreadRequest request) {
        User user = userService.getAuthenticatedUser();
        Reply reply = new Reply();
        Thread thread;
        if (request.getThreadId() != null) {
            thread = threadRepository.findById(request.getThreadId())
                    .orElseThrow(() -> new ThreadNotFoundException("Thread not found"));
            reply.setThread(thread);
        }

        if (request.getParentReplyId() != null) {
            Reply parentReply = replyRepository.findById(request.getParentReplyId())
                    .orElseThrow(() -> new ReplyNotFoundException("Reply not found"));
            reply.setParentReply(parentReply);
        }
        reply.setContent(request.getContent());
        reply.setUser(user);
        return replyRepository.save(reply);
    }

    @Override
    public Page<Reply> getRepliesForThread(Long threadId, Pageable pageable) {
        return replyRepository.findByThreadId(threadId, pageable);
    }

    @Override
    public Page<Reply> getRepliesForParentReply(Long parentReplyId, Pageable pageable) {
        return replyRepository.findByParentReplyId(parentReplyId, pageable);
    }

    @Override
    public void deleteReply(Long replyId) {
        validateAuthenticatedUserOwnerOfEntity(replyId);
        replyRepository.deleteById(replyId);
    }

    private void validateAuthenticatedUserOwnerOfEntity(Long replyId) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new ReplyNotFoundException("Reply not found " + replyId));
        User currentUser = userService.getAuthenticatedUser();
        if (!reply.getUser().equals(currentUser)) {
            throw new AccessDeniedException("You are not authorized to delete this reply");
        }
    }

    @Override
    public Reply editReply(Long replyId, EditReplyRequest request) {
        validateAuthenticatedUserOwnerOfEntity(replyId);
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new ReplyNotFoundException("Reply not found " + replyId));
        reply.setContent(request.getContent());
        return replyRepository.save(reply);
    }
}
