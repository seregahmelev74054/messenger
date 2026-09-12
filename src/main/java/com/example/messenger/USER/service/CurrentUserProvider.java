package com.example.messenger.USER.service;

import com.example.messenger.USER.entity.User;
import com.example.messenger.USER.repository.UserRepository;
import com.example.messenger.EXCEPTION.UserLoginException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserProvider {

    final private UserRepository userRepository;

    CurrentUserProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {

        long userId = (long) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return userRepository.findById(userId)
                .orElseThrow(() -> new UserLoginException("Access denied"));
    }
}
