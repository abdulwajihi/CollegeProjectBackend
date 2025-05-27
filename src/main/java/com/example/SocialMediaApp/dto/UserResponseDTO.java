package com.example.SocialMediaApp.dto;

import lombok.Data;

@Data
public class UserResponseDTO {//Follower and Following Dto
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
}
