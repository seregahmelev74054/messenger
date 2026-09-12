package com.example.messenger.USER.dto.login.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginUserRequest {

    @NotBlank(message = "the login/email shouldn't be empty")
    @Size(min = 3, max = 254)
    private String loginOrEmail;

    @NotBlank(message = "the password shouldn't be empty")
    @Size(min = 8, max = 32, message = "the password length must be from 8 to 32 characters")
    private String password;

    public void setLoginOrEmail(String email) {
        this.loginOrEmail = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLoginOrEmail() {
        return loginOrEmail;
    }

    public String getPassword() {
        return password;
    }

}
