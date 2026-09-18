package com.example.messenger.USER.dto.currentUser;

import com.example.messenger.USER.entity.User;

import java.time.Instant;

public class CurrentUserResponse {

    public CurrentUserResponse(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.email = user.getEmail();
        this.displayName = user.getDisplayName();
        this.createdAt = user.getCreatedAt();
    }

    private long id;

    private String login;

    private String email;

    private String displayName;

    private Instant createdAt;
}
