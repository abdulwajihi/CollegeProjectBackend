package com.example.SocialMediaApp.dto;

import lombok.Data;

@Data
public class VerifySigninRequest {
    private String email;
    private String otp;
}
