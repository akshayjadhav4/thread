package com.clone.threadclone.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clone.threadclone.model.User;
import com.clone.threadclone.request.CreateUserRequest;
import com.clone.threadclone.request.LoginRequest;
import com.clone.threadclone.response.ApiResponse;
import com.clone.threadclone.response.JwtResponse;
import com.clone.threadclone.security.jwt.JwtUtils;
import com.clone.threadclone.security.user.UserProfileDetails;
import com.clone.threadclone.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            return authenticateAndGetJwtResponse(request.getEmail(), request.getPassword(), "Login Successful");
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    private ResponseEntity<ApiResponse> authenticateAndGetJwtResponse(String email, String password,
            String successMessage) {
        // Authenticate the credentials using AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT token
        String jwt = jwtUtils.generateTokenForUser(authentication);

        // Retrieve authenticated user details
        UserProfileDetails userProfileDetails = (UserProfileDetails) authentication.getPrincipal();

        // Create JWT response with user ID and token
        JwtResponse jwtResponse = new JwtResponse(userProfileDetails.getId(), jwt);

        return ResponseEntity.ok(new ApiResponse(successMessage, jwtResponse));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest request) {
        try {
            User user = userService.createUser(request);
            // Authenticate the new user and generate the JWT response
            return authenticateAndGetJwtResponse(user.getEmail(), request.getPassword(),
                    "Account Created Successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
