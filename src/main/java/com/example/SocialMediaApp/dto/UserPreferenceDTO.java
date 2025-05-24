package com.example.SocialMediaApp.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPreferenceDTO {
    private Long id;
    private Long preferenceTypeId;
    private String preferenceTypeName;
}