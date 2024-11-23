package com.clone.threadclone.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.clone.threadclone.exceptions.ThreadNotFoundException;
import com.clone.threadclone.exceptions.UserNotFoundException;
import com.clone.threadclone.helpers.Helpers;
import com.clone.threadclone.model.Hashtag;
import com.clone.threadclone.model.Media;
import com.clone.threadclone.model.Thread;
import com.clone.threadclone.model.ThreadHashtag;
import com.clone.threadclone.model.User;
import com.clone.threadclone.model.Media.MediaType;
import com.clone.threadclone.repository.HashtagRepository;
import com.clone.threadclone.repository.ThreadHashtagRepository;
import com.clone.threadclone.repository.ThreadRepository;
import com.clone.threadclone.repository.UserRepository;
import com.clone.threadclone.request.CreateThreadRequest;
import com.clone.threadclone.request.UpdateThreadRequest;
import com.clone.threadclone.service.ThreadService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ThreadServiceImpl implements ThreadService {

    private final ThreadRepository threadRepository;
    private final UserRepository userRepository;
    private final HashtagRepository hashtagRepository;
    private final ThreadHashtagRepository threadHashtagRepository;

    @Override
    public Thread createThread(CreateThreadRequest request) {
        User user = getAuthenticatedUser();
        Thread thread = new Thread();
        thread.setContent(request.getContent());
        if (request.getMedia() != null && !request.getMedia().isEmpty()) {
            List<Media> medias = request.getMedia().stream()
                    .map(mediaRequest -> {
                        if (!isValidMediaType(mediaRequest.getUrl(), mediaRequest.getType())) {
                            throw new IllegalArgumentException("Invalid media type for URL: " + mediaRequest.getUrl());
                        }
                        Media media = new Media();
                        media.setUrl(mediaRequest.getUrl());
                        media.setType(mediaRequest.getType());
                        media.setThread(thread);
                        return media;
                    })
                    .collect(Collectors.toList());
            thread.setMedias(medias);
        }
        thread.setUser(user);
        Thread savedThread = threadRepository.save(thread);
        List<String> hashtags = Helpers.getAllHashtags(request.getContent());
        if (hashtags != null && !hashtags.isEmpty()) {
            processHashtags(hashtags, savedThread);
        }
        return savedThread;
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("Authenticated user not found.");
        }
        return user;
    }

    private boolean isValidMediaType(String url, MediaType type) {
        if (url.endsWith(".jpg") || url.endsWith(".png")) {
            return type == MediaType.IMAGE;
        } else if (url.endsWith(".mp4")) {
            return type == MediaType.VIDEO;
        } else if (url.endsWith(".gif")) {
            return type == MediaType.GIF;
        }
        return false;
    }

    private void processHashtags(List<String> hashtags, Thread thread) {
        List<Hashtag> existingHashtags = hashtagRepository.findByNameIn(hashtags);

        Set<String> existingHashtagNames = existingHashtags.stream()
                .map(Hashtag::getName)
                .collect(Collectors.toSet());
        List<String> newHashtags = hashtags.stream()
                .filter(tag -> !existingHashtagNames.contains(tag))
                .collect(Collectors.toList());

        List<Hashtag> hashtagsToInsert = newHashtags.stream()
                .map(name -> {
                    Hashtag hashtag = new Hashtag();
                    hashtag.setName(name);
                    return hashtag;
                })
                .collect(Collectors.toList());

        List<Hashtag> savedHashtags = new ArrayList<>();
        if (!hashtagsToInsert.isEmpty()) {
            Iterable<Hashtag> iterable = hashtagRepository.saveAll(hashtagsToInsert);
            savedHashtags = StreamSupport.stream(iterable.spliterator(), false)
                    .collect(Collectors.toList());

        }
        List<Hashtag> allHashtags = new ArrayList<>(existingHashtags);
        allHashtags.addAll(savedHashtags);
        // Create ThreadHashtag associations
        List<ThreadHashtag> threadHashtags = allHashtags.stream()
                .map(ht -> {
                    ThreadHashtag threadHashtag = new ThreadHashtag();
                    threadHashtag.setHashtag(ht);
                    threadHashtag.setThread(thread);
                    return threadHashtag;
                })
                .collect(Collectors.toList());

        if (!threadHashtags.isEmpty()) {
            threadHashtagRepository.saveAll(threadHashtags);
        }

    }

    @Override
    public Thread getThreadById(Long threadId) {
        return threadRepository.findById(threadId)
                .orElseThrow(() -> new ThreadNotFoundException("Thread not found " + threadId));
    }

    @Override
    public void deleteThread(Long threadId) {
        threadRepository.deleteById(threadId);
    }

    @Override
    public Page<Thread> getAllThreads(Pageable pageable) {
        return threadRepository.findAll(pageable);
    }

    @Override
    public Page<Thread> getThreadsByUser(Long userId, Pageable pageable) {
        return threadRepository.findByUserId(userId, pageable);
    }

    @Override
    public Page<Thread> getThreadsByHashtag(String hashtag, Pageable pageable) {
        return threadRepository.findByHashtagName(hashtag, pageable);
    }

    @Transactional
    @Override
    public Thread editThread(Long threadId, UpdateThreadRequest request) {
        Thread existingThread = threadRepository.findById(threadId)
                .orElseThrow(() -> new ThreadNotFoundException("Thread not found"));
        User currentUser = getAuthenticatedUser();
        if (!existingThread.getUser().equals(currentUser)) {
            throw new AccessDeniedException("You are not authorized to edit this thread");
        }
        existingThread.setContent(request.getContent());

        if (request.getMediaToRemove() != null && !request.getMediaToRemove().isEmpty()) {
            existingThread.getMedias().removeIf(media -> request.getMediaToRemove().contains(media.getId()));
        }
        if (request.getMediaToAdd() != null && !request.getMediaToAdd().isEmpty()) {
            List<Media> medias = request.getMediaToAdd().stream()
                    .map(mediaRequest -> {
                        if (!isValidMediaType(mediaRequest.getUrl(), mediaRequest.getType())) {
                            throw new IllegalArgumentException("Invalid media type for URL: " + mediaRequest.getUrl());
                        }
                        Media media = new Media();
                        media.setUrl(mediaRequest.getUrl());
                        media.setType(mediaRequest.getType());
                        media.setThread(existingThread);
                        return media;
                    })
                    .collect(Collectors.toList());
            existingThread.getMedias().addAll(medias);
        }

        // Step1 extract all hashtags from new content
        List<String> hashtagsExtractedFromUpdatedContent = Helpers.getAllHashtags(request.getContent());

        // Step2 extract all hashtags from existing content
        List<String> existingHashtagsInContent = existingThread.getThreadHashtags().stream()
                .map(threadHashtag -> threadHashtag.getHashtag().getName())
                .collect(Collectors.toList());

        // Step3 Find hashtags in the new content but not in the existing content (new
        // hashtags)
        Set<String> newHashtags = new HashSet<>(hashtagsExtractedFromUpdatedContent);
        newHashtags.removeAll(existingHashtagsInContent);

        // Step4 Find hashtags in the existing content but not in the new content
        // (removed
        // hashtags)
        Set<String> removedHashtags = new HashSet<>(existingHashtagsInContent);
        removedHashtags.removeAll(hashtagsExtractedFromUpdatedContent);

        // Step5 Save new hashtags that were added
        if (!newHashtags.isEmpty()) {
            // Step5.1 Save new hashtags in the database if they don't exist yet
            List<Hashtag> hashtagsToSave = newHashtags.stream()
                    .filter(name -> !hashtagRepository.existsByName(name)) // Avoid duplicates
                    .map(name -> {
                        Hashtag hashtag = new Hashtag();
                        hashtag.setName(name);
                        return hashtag;
                    }) // Create new Hashtag objects
                    .collect(Collectors.toList());
            hashtagRepository.saveAll(hashtagsToSave);

            // Step5.2 Add new hashtags to the thread's threadHashtags relationship
            for (String hashtagName : newHashtags) {
                Hashtag hashtag = hashtagRepository.findByName(hashtagName);
                ThreadHashtag threadHashtag = new ThreadHashtag();
                threadHashtag.setHashtag(hashtag);
                threadHashtag.setThread(existingThread);
                existingThread.getThreadHashtags().add(threadHashtag);
            }
        }
        // Step6 Remove hashtags that were removed from the content
        if (!removedHashtags.isEmpty()) {
            for (String hashtagName : removedHashtags) {
                Hashtag hashtag = hashtagRepository.findByName(hashtagName);
                ThreadHashtag threadHashtagToRemove = existingThread.getThreadHashtags().stream()
                        .filter(th -> th.getHashtag().equals(hashtag))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Hashtag not found"));

                existingThread.getThreadHashtags().remove(threadHashtagToRemove);
            }
        }
        return threadRepository.save(existingThread);
    }
}
