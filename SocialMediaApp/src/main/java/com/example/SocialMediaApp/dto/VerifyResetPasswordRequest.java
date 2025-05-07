package com.example.SocialMediaApp.dto;

import lombok.Data;

@Data
public class VerifyResetPasswordRequest {
    private String email;
    private String otp;
    private String newPassword;
}
