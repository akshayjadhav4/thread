package com.clone.threadclone.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.clone.threadclone.model.User;
import com.clone.threadclone.request.CreateUserRequest;
import com.clone.threadclone.request.UpdateUserRequest;

public interface UserService {

    User getUserById(Long userId);

    User createUser(CreateUserRequest request);

    User updateUser(UpdateUserRequest request, Long userId);

    User getAuthenticatedUser();

    String followUser(Long followedId);

    String unfollowUser(Long followedId);

    Page<User> getFollowers(Long userId, Pageable pageable);

    Page<User> getFollowing(Long userId, Pageable pageable);
}
