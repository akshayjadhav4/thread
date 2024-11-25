package com.clone.threadclone.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clone.threadclone.mapper.impl.ReplyMapper;
import com.clone.threadclone.model.Reply;
import com.clone.threadclone.request.EditReplyRequest;
import com.clone.threadclone.request.ReplyToThreadRequest;
import com.clone.threadclone.response.ApiResponse;
import com.clone.threadclone.service.ReplyService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/reply")
public class ReplyController {

    private final ReplyService replyService;
    private final ReplyMapper replyMapper;

    @PostMapping("/")
    public ResponseEntity<ApiResponse> replyToThread(@RequestBody ReplyToThreadRequest request) {
        try {
            Reply reply = replyService.replyToThread(request);
            return ResponseEntity.ok(new ApiResponse("Replied successfully", replyMapper.mapTo(reply, false)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping(path = "/list")
    public ResponseEntity<ApiResponse> getReplies(@RequestParam(required = false) Long threadId,
            @RequestParam(required = false) Long replyId,
            @RequestParam(required = false) Boolean includeThreadDetails,
            Pageable pageable) {
        try {
            if (threadId == null && replyId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse("Provide either Thread ID or Reply ID", null));
            }

            Page<Reply> replies;
            String message;

            if (threadId != null) {
                replies = replyService.getRepliesForThread(threadId, pageable);
                message = "Replies for thread fetched successfully";
            } else {
                replies = replyService.getRepliesForParentReply(replyId, pageable);
                message = "Replies for reply fetched successfully";
            }
            boolean includeThread = includeThreadDetails != null && includeThreadDetails;
            return ResponseEntity
                    .ok(new ApiResponse(message, replies.map(reply -> replyMapper.mapTo(reply, includeThread))));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{replyId}")
    public ResponseEntity<ApiResponse> deleteThread(@PathVariable Long replyId) {
        try {
            replyService.deleteReply(replyId);
            return ResponseEntity.ok(new ApiResponse("Reply Deleted", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{replyId}")
    public ResponseEntity<ApiResponse> editReply(@PathVariable Long replyId,
            @RequestBody EditReplyRequest request) {
        try {
            Reply reply = replyService.editReply(replyId, request);
            return ResponseEntity.ok(new ApiResponse("Edit successful.", replyMapper.mapTo(reply, false)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        }
    }

}