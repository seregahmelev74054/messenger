package com.example.messenger.USER.dto.register;

import jakarta.validation.constraints.*;

public class RegisterUserRequest {

    @NotBlank(message = "the login shouldn't be empty")
    @Size(min = 4, max = 20, message = "the login length must be from 4 to 20 characters")
    private String login;

    @NotBlank(message = "the name shouldn't be empty")
    @Size(min = 4, max = 20, message = "the name length must be from 4 to 20 characters")
    private String displayName;

    @NotBlank(message = "the email shouldn't be empty")
    @Email
    private String email;

    @NotBlank(message = "the password shouldn't be empty")
    @Size(min = 8, max = 32, message = "the password length must be from 8 to 32 characters")
    private String password;

    public void setLogin(String login) {
        this.login = login;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
