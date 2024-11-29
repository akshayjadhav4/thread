package com.clone.threadclone.service.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.clone.threadclone.exceptions.UserNotFoundException;
import com.clone.threadclone.model.User;
import com.clone.threadclone.repository.UserRepository;
import com.clone.threadclone.request.CreateUserRequest;
import com.clone.threadclone.request.UpdateUserRequest;
import com.clone.threadclone.service.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found " + userId));
    }

    @Override
    public User createUser(CreateUserRequest request) {
        return Optional.of(request)
                .filter(user -> !userRepository.existsByEmail(request.getEmail()))
                .map(req -> {
                    User user = new User();
                    user.setUsername(request.getUsername());
                    user.setDisplayName(request.getDisplayName());
                    user.setEmail(request.getEmail());
                    user.setPassword(passwordEncoder.encode(request.getPassword()));
                    user.setBio(request.getBio());
                    user.setProfilePicture(request.getProfilePicture());

                    return userRepository.save(user);
                }).orElseThrow(() -> new RuntimeException("User already exists"));
    }

    @Override
    public User updateUser(UpdateUserRequest request, Long userId) {
        return userRepository.findById(userId).map(existingUser -> {
            existingUser.setDisplayName(request.getDisplayName());
            existingUser.setUsername(request.getUsername());
            existingUser.setBio(request.getBio());
            existingUser.setProfilePicture(request.getProfilePicture());
            return userRepository.save(existingUser);
        }).orElseThrow(() -> new RuntimeException("User not found " + userId));
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("Authenticated user not found.");
        }
        return user;
    }

    @Override
    @Transactional
    public String followUser(Long followedId) {

        User follower = getAuthenticatedUser();
        User followed = userRepository.findById(followedId)
                .orElseThrow(() -> new RuntimeException("Followed user not found"));
        if (follower.getId().equals(followedId)) {
            return "You can't follow yourself. Try making some friends instead.";
        } else if (follower.getFollowing().contains(followed)) {
            return "You are already following this user.";
        }
        userRepository.followUser(follower.getId(), followedId);
        return "Successfully followed user!";
    }

    @Override
    @Transactional
    public String unfollowUser(Long followedId) {
        User follower = getAuthenticatedUser();
        User followed = userRepository.findById(followedId)
                .orElseThrow(() -> new RuntimeException("Followed user not found"));
        if (!follower.getFollowing().contains(followed)) {
            return "You are not following this user.";
        }
        userRepository.unfollowUser(follower.getId(), followedId);
        return "Successfully unfollowed user!";
    }

    @Override
    public Page<User> getFollowers(Long userId, Pageable pageable) {
        return userRepository.findFollowersByUserId(userId, pageable);
    }

    @Override
    public Page<User> getFollowing(Long userId, Pageable pageable) {
        return userRepository.findFollowingByUserId(userId, pageable);
    }

}
