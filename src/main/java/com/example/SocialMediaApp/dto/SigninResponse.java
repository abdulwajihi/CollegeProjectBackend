package com.example.SocialMediaApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class SigninResponse {
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private List<String> preferences;
    private String token;
    private String id;


    public SigninResponse(String token, User user, List<String> preferenceList) {
        this.token = token;
        this.id= String.valueOf(user.getId());
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.preferences = preferenceList;
    }
}
