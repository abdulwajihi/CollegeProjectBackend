package com.example.SocialMediaApp.util;

public class PasswordValidator {

    private static final String PASSWORD_PATTERN =
            "^(?=.*[A-Z])(?=.*[!@#$%^&*()_+=\\-{}|:;\"'<>,.?/~`]).{8,}$";

    public static boolean isValid(String password) {
        return password != null && password.matches(PASSWORD_PATTERN);
    }
}
