package com.clone.threadclone.request;

import lombok.Data;

@Data
public class CreateUserRequest {

    private String displayName;

    private String username;

    private String email;

    private String password;

    private String profilePicture;

    private String bio;
}
