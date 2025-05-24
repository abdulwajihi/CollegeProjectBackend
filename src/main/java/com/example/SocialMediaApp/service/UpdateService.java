package com.example.SocialMediaApp.service;

import com.example.SocialMediaApp.dto.*;
import com.example.SocialMediaApp.repository.PreferenceTypeRepository;
import com.example.SocialMediaApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UpdateService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PreferenceTypeRepository preferenceTypeRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    public UserProfileDTO getProfile(String username) {
        String authenticatedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!authenticatedUsername.equals(username)) {
            throw new IllegalArgumentException("You can only access your own profile");
        }

        User user = userRepository.findByEmailOrUsernameWithPreferences(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
        return convertToProfileDTO(user);
    }

    @Transactional
    public UserProfileDTO updateProfile(String username, UserProfileDTO profileDTO, MultipartFile profilePicture) throws IOException {
        String authenticatedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!authenticatedUsername.equals(username)) {
            throw new IllegalArgumentException("You can only update your own profile");
        }

        User user = userRepository.findByEmailOrUsernameWithPreferences(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        // Update only the fields that are provided (non-null)
        if (profileDTO.getFirstName() != null) {
            user.setFirstName(profileDTO.getFirstName());
        }
        if (profileDTO.getLastName() != null) {
            user.setLastName(profileDTO.getLastName());
        }
        if (profileDTO.getGender() != null) {
            user.setGender(profileDTO.getGender());
        }
        if (profileDTO.getBio() != null) {
            user.setBio(profileDTO.getBio());
        }

        // Update preferences if provided
        if (profileDTO.getPreferences() != null) {
            user.getPreferences().clear();
            List<UserPreference> newPreferences = profileDTO.getPreferences().stream().map(dto -> {
                UserPreference pref = new UserPreference();
                pref.setUser(user);
                PreferenceType prefType = preferenceTypeRepository.findById(dto.getPreferenceTypeId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid preference type ID: " + dto.getPreferenceTypeId()));
                pref.setPreferenceType(prefType);

                return pref;
            }).collect(Collectors.toList());
            user.getPreferences().addAll(newPreferences);

            // Update preferenceNames field
            List<String> preferenceNames = newPreferences.stream()
                    .map(pref -> pref.getPreferenceType().getName())
                    .collect(Collectors.toList());
            user.setPreferenceNames(String.join(",", preferenceNames));
        }

        // Update profile picture if provided (disabled for now due to Cloudinary issue)
        if (profilePicture != null && !profilePicture.isEmpty()) {
             String profilePictureUrl = cloudinaryService.uploadProfilePicture(profilePicture, user.getId());
             user.setProfilePictureUrl(profilePictureUrl);
//            user.setProfilePictureUrl("temp-url-for-testing"); // Hardcoded for now
        }

        userRepository.save(user);
        return convertToProfileDTO(user);
    }

    private UserProfileDTO convertToProfileDTO(User user) {
        UserProfileDTO dto = new UserProfileDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setGender(user.getGender());
        dto.setBio(user.getBio());
        dto.setProfilePictureUrl(user.getProfilePictureUrl());

        List<UserPreferenceDTO> preferenceDTOs = user.getPreferences().stream().map(pref -> {
            UserPreferenceDTO prefDTO = new UserPreferenceDTO();
            prefDTO.setId(pref.getId());
            prefDTO.setPreferenceTypeId(pref.getPreferenceType().getId());
            prefDTO.setPreferenceTypeName(pref.getPreferenceType().getName());
            return prefDTO;
        }).collect(Collectors.toList());
        dto.setPreferences(preferenceDTOs);

        return dto;
    }

}