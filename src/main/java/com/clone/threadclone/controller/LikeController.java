package com.clone.threadclone.controller;

import com.clone.threadclone.kafka.producer.ProduceThreadLike;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;

import com.clone.threadclone.dto.LikeDto;
import com.clone.threadclone.mapper.Mapper;
import com.clone.threadclone.model.Like;
import com.clone.threadclone.response.ApiResponse;
import com.clone.threadclone.response.LikeResponse;
import com.clone.threadclone.service.LikeService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/like")
public class LikeController {

    private final LikeService likeService;

    private  final ProduceThreadLike produceThreadLike;

    private final Mapper<Like, LikeDto> likeMapper;

    @PostMapping("/thread/{threadId}")
    public ResponseEntity<ApiResponse> likeThread(@PathVariable Long threadId) {
        try {
            produceThreadLike.sendLike("Like for thread ID" + threadId);
            int likeCount = likeService.likeThread(threadId);
            LikeResponse response = new LikeResponse(likeCount, threadId, null);
            return ResponseEntity.ok(new ApiResponse("Thread liked successfully", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/thread/{threadId}")
    public ResponseEntity<ApiResponse> unlikeThread(@PathVariable Long threadId) {
        try {
            int likeCount = likeService.unlikeThread(threadId);
            LikeResponse response = new LikeResponse(likeCount, threadId, null);
            return ResponseEntity.ok(new ApiResponse("Thread unliked successfully", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/reply/{replyId}")
    public ResponseEntity<ApiResponse> likeReply(@PathVariable Long replyId) {
        try {
            int likeCount = likeService.likeReply(replyId);
            LikeResponse response = new LikeResponse(likeCount, null, replyId);
            return ResponseEntity.ok(new ApiResponse("Reply liked successfully", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/reply/{replyId}")
    public ResponseEntity<ApiResponse> unlikeReply(@PathVariable Long replyId) {
        try {
            int likeCount = likeService.unlikeReply(replyId);
            LikeResponse response = new LikeResponse(likeCount, null, replyId);
            return ResponseEntity.ok(new ApiResponse("Reply unliked successfully", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping(path = "/list")
    public ResponseEntity<ApiResponse> getLikes(
            @RequestParam(required = false) Long threadId,
            @RequestParam(required = false) Long replyId,
            Pageable pageable) {
        try {
            // Validate that only one parameter is provided
            if ((threadId == null && replyId == null) || (threadId != null && replyId != null)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse("Provide either threadId or replyId, but not both.", null));
            }
            if (threadId != null) {
                Page<Like> threadLikes = likeService.getThreadLikes(threadId, pageable);
                return ResponseEntity.ok(
                        new ApiResponse("Likes for thread fetched successfully", threadLikes.map(likeMapper::mapTo)));
            }
            Page<Like> replyLikes = likeService.getReplyLikes(replyId, pageable);
            return ResponseEntity
                    .ok(new ApiResponse("Likes for reply fetched successfully", replyLikes.map(likeMapper::mapTo)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
