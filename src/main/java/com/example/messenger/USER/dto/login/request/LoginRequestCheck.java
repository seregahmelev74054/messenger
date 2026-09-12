package com.example.messenger.USER.dto.login.request;

import java.util.regex.Pattern;

public class LoginRequestCheck {

    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                    "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$";

    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    static public LoginRequestType check(String emailOrLogin) {

        if (EMAIL_PATTERN.matcher(emailOrLogin).matches())
            return LoginRequestType.EMAIL;

        if (emailOrLogin.length() >= 4 && emailOrLogin.length() <= 20)
            return LoginRequestType.LOGIN;

        return LoginRequestType.WRONG;
    }
}