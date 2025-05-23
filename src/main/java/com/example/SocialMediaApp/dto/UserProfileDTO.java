package com.example.SocialMediaApp.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String gender;
    private String bio;
    private String profilePictureUrl;
    private List<UserPreferenceDTO> preferences;
}