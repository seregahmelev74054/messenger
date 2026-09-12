package com.example.messenger.USER.service;

import com.example.messenger.USER.dto.currentUser.CurrentUserResponse;
import com.example.messenger.USER.dto.login.request.LoginRequestCheck;
import com.example.messenger.USER.dto.login.request.LoginRequestType;
import com.example.messenger.USER.dto.login.request.LoginUserRequest;
import com.example.messenger.USER.dto.login.response.LoginUserResponse;
import com.example.messenger.USER.dto.register.RegisterUserRequest;
import com.example.messenger.JWT.JwtService;
import com.example.messenger.USER.entity.User;
import com.example.messenger.EXCEPTION.UserAlreadyExistsException;
import com.example.messenger.EXCEPTION.UserLoginException;
import com.example.messenger.USER.repository.UserRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final BCryptPasswordEncoder encoder;

    private final CurrentUserProvider currentUserProvider;

    public UserService(
            UserRepository userRepository,
            JwtService jwtService,
            BCryptPasswordEncoder encoder,
            CurrentUserProvider currentUserProvider
    ) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.encoder = encoder;
        this.currentUserProvider = currentUserProvider;
    }

    @Transactional
    public void register(RegisterUserRequest request) {

        if (userRepository.existsByLogin(request.getLogin())) {
            throw new UserAlreadyExistsException("User with ' " + request.getLogin() + " ' login exists already" );
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("User with ' " + request.getEmail() + " ' email exists already" );
        }

        User user = new User();

        user.setLogin(request.getLogin());
        user.setEmail(request.getEmail());
        user.setPasswordHash(
                encoder.encode(request.getPassword())
        );
        user.setDisplayName(request.getDisplayName());

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public LoginUserResponse login(LoginUserRequest request) {

        User user;

        LoginRequestType type = LoginRequestCheck.check(request.getLoginOrEmail());

        if (type == LoginRequestType.EMAIL)
            user = userRepository.findByEmail( request.getLoginOrEmail() )
                    .orElseThrow(() -> new UserLoginException("Login failed"));

        else if (type == LoginRequestType.LOGIN)
            user = userRepository.findByLogin( request.getLoginOrEmail() )
                    .orElseThrow(() -> new UserLoginException("Login failed"));

        else throw new UserLoginException("Login failed");

        if (encoder.matches(request.getPassword(), user.getPasswordHash())) {
            String token = jwtService.generateToken(user);

            return new LoginUserResponse(token);
        }
        else throw new UserLoginException("Login failed");
    }

    @Transactional(readOnly = true)
    public CurrentUserResponse currentUser() {

        User currentUser = currentUserProvider.getCurrentUser();

        return new CurrentUserResponse(currentUser);
    }
}
