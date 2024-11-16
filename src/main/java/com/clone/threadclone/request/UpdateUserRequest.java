package com.clone.threadclone.request;

import lombok.Data;

@Data
public class UpdateUserRequest {

    private String displayName;

    private String username;

    private String profilePicture;

    private String bio;
}
