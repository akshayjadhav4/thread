package com.clone.threadclone.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.clone.threadclone.model.Thread;
import com.clone.threadclone.request.CreateThreadRequest;
import com.clone.threadclone.request.UpdateThreadRequest;

public interface ThreadService {

    Thread createThread(CreateThreadRequest request);

    Thread getThreadById(Long threadId);

    Thread editThread(Long threadId, UpdateThreadRequest request);

    void deleteThread(Long threadId);

    Page<Thread> getAllThreads(Pageable pageable);

    Page<Thread> getThreadsByUser(Long userId, Pageable pageable);

    Page<Thread> getThreadsByHashtag(String hashtag, Pageable pageable);
}
