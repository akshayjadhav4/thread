package com.clone.threadclone.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clone.threadclone.dto.UserDto;
import com.clone.threadclone.mapper.Mapper;
import com.clone.threadclone.model.User;
import com.clone.threadclone.request.CreateUserRequest;
import com.clone.threadclone.request.UpdateUserRequest;
import com.clone.threadclone.response.ApiResponse;
import com.clone.threadclone.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {

    private final UserService userService;
    private final Mapper<User, UserDto> userMapper;
    private static final String SUCCESS_MESSAGE = "Success";

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId) {
        try {
            User user = userService.getUserById(userId);
            return ResponseEntity.ok(new ApiResponse(SUCCESS_MESSAGE, userMapper.mapTo(user)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/")
    public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest request) {
        try {
            User user = userService.createUser(request);
            return ResponseEntity.ok(new ApiResponse(SUCCESS_MESSAGE, userMapper.mapTo(user)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable Long userId, @RequestBody UpdateUserRequest request) {
        try {
            User user = userService.updateUser(request, userId);
            return ResponseEntity.ok(new ApiResponse(SUCCESS_MESSAGE, userMapper.mapTo(user)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
