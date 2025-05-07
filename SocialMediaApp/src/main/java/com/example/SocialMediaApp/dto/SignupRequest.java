package com.example.SocialMediaApp.dto;

import lombok.Data;
import java.util.List;

@Data
public class SignupRequest {
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String gender;
    private List<String> preferences;
}

