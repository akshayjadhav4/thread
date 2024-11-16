package com.clone.threadclone.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.clone.threadclone.model.User;
import com.clone.threadclone.repository.UserRepository;
import com.clone.threadclone.request.CreateUserRequest;
import com.clone.threadclone.request.UpdateUserRequest;
import com.clone.threadclone.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found" + userId));
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
                    user.setPassword(request.getPassword());
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
        }).orElseThrow(() -> new RuntimeException("User not found" + userId));
    }

}
