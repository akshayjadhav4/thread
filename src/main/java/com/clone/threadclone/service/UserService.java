package com.clone.threadclone.service;

import com.clone.threadclone.model.User;
import com.clone.threadclone.request.CreateUserRequest;
import com.clone.threadclone.request.UpdateUserRequest;

public interface UserService {

    User getUserById(Long userId);

    User createUser(CreateUserRequest request);

    User updateUser(UpdateUserRequest request, Long userId);
}
