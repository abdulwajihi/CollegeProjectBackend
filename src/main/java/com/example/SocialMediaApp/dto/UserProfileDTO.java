package com.example.SocialMediaApp.dto;

import jakarta.persistence.JoinColumn;
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
    private String bio;
    private String profilePictureUrl;

    private List<UserPreferenceDTO> preferences;// ✅ User preferences
    private List<ImageResponseDto> images;
    public List<ImageResponseDto> getImages() {
        return images;
    }

    public void setImages(List<ImageResponseDto> images) {
        this.images = images;
    }

}