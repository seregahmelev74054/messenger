package com.example.messenger.USER.controller;

import com.example.messenger.USER.dto.currentUser.CurrentUserResponse;
import com.example.messenger.USER.dto.login.request.LoginUserRequest;
import com.example.messenger.USER.dto.login.response.LoginUserResponse;
import com.example.messenger.USER.dto.register.RegisterUserRequest;

import com.example.messenger.USER.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    public UserController (
            UserService userService
    ) {
        this.userService = userService;
    }

    @PostMapping("/api/v1/users")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterUserRequest request) {

        userService.register(request);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/api/v1/auth/login")
    public ResponseEntity<Void> login(
            @Valid @RequestBody LoginUserRequest request,
            HttpServletResponse response
    ) {

        ResponseCookie cookie = userService.login(request);

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/v1/users/me")
    public ResponseEntity<CurrentUserResponse> currentUser() {
        CurrentUserResponse response = userService.currentUser();

        return ResponseEntity.ok(response);
    }
}
