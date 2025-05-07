package com.example.SocialMediaApp.controller;


import com.example.SocialMediaApp.dto.PreferenceType;
import com.example.SocialMediaApp.dto.UserPreferenceProjection;
import com.example.SocialMediaApp.repository.PreferenceRepository;
import com.example.SocialMediaApp.repository.PreferenceTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/preferences")
public class PreferenceController {

    @Autowired
    private PreferenceTypeRepository preferenceTypeRepository;

    @GetMapping
    public List<PreferenceType> getAllPreferences() {
        return preferenceTypeRepository.findAll();
    }
    @Autowired
    private PreferenceRepository userPreferenceRepository;

    @GetMapping("/all")
    public List<UserPreferenceProjection> getAllUserPreferences() {
        return userPreferenceRepository.getUserPreferencesWithNames();
    }
}

