package com.example.messenger.USER.dto.login.response;

public class LoginUserResponse {

    public LoginUserResponse (String token) {
        this.token = token;
    }

    private final String token;

    public String getToken() {
        return token;
    }
}
