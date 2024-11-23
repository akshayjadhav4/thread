package com.clone.threadclone.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clone.threadclone.model.Thread;
import com.clone.threadclone.request.CreateThreadRequest;
import com.clone.threadclone.request.UpdateThreadRequest;
import com.clone.threadclone.response.ApiResponse;
import com.clone.threadclone.service.ThreadService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/thread")
public class ThreadController {

    private final ThreadService threadService;

    @PostMapping("/")
    public ResponseEntity<ApiResponse> createThread(@RequestBody CreateThreadRequest request) {
        try {
            Thread thread = threadService.createThread(request);
            return ResponseEntity.ok(new ApiResponse("Thread Created", thread));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{threadId}")
    public ResponseEntity<ApiResponse> getThreadById(@PathVariable Long threadId) {
        try {
            Thread thread = threadService.getThreadById(threadId);
            return ResponseEntity.ok(new ApiResponse("Success", thread));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{threadId}")
    public ResponseEntity<ApiResponse> deleteThread(@PathVariable Long threadId) {
        try {
            threadService.deleteThread(threadId);
            return ResponseEntity.ok(new ApiResponse("Thread Deleted", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping(path = "/all")
    public ResponseEntity<ApiResponse> getAllThreads(Pageable pageable) {
        try {
            Page<Thread> threads = threadService.getAllThreads(pageable);
            return ResponseEntity.ok(new ApiResponse("Threads fetched successfully", threads));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping(path = "/user/{userId}")
    public ResponseEntity<ApiResponse> getThreadsByUser(@PathVariable Long userId, Pageable pageable) {
        try {
            Page<Thread> threads = threadService.getThreadsByUser(userId, pageable);
            return ResponseEntity.ok(new ApiResponse("Threads fetched successfully", threads));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping(path = "/hashtag/{hashtag}")
    public ResponseEntity<ApiResponse> getThreadsByHashtag(@PathVariable String hashtag, Pageable pageable) {
        try {
            Page<Thread> threads = threadService.getThreadsByHashtag("#" + hashtag, pageable);
            return ResponseEntity.ok(new ApiResponse("Threads fetched successfully", threads));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{threadId}")
    public ResponseEntity<ApiResponse> editThread(@PathVariable Long threadId,
            @RequestBody UpdateThreadRequest request) {
        try {
            Thread thread = threadService.editThread(threadId, request);
            return ResponseEntity.ok(new ApiResponse("Edit successful.", thread));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
