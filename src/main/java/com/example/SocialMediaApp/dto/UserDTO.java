package com.example.SocialMediaApp.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;                // Optional: For frontend reference
    private String username;
    private String email;
    private String firstName;
    private String lastName;


//    public UserDTO(Long id, String username, String email, String firstName, String lastName, String gender, String phoneNumber) {
//    }

    // Optional: Show if account is verified
}

