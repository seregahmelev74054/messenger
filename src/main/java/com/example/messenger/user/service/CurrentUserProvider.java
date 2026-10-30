package com.example.messenger.user.service;

import com.example.messenger.user.entity.User;
import com.example.messenger.user.repository.UserRepository;
import com.example.messenger.exception.UserLoginException;
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
