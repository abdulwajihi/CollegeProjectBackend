package com.example.SocialMediaApp.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity//Entity User Preference
@Table(name = "user_preference")
@Data
public class UserPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "preference_type_id")
    private PreferenceType preferenceType;


    private String preferenceName;

//    @Column(name = "preference_value")
//    private String preferenceValue; // Added to store the value of the preference
}