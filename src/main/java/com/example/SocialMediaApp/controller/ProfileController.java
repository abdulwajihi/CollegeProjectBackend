package com.example.SocialMediaApp.controller;

import com.example.SocialMediaApp.dto.ImageResponseDto;
import com.example.SocialMediaApp.dto.UserProfileDTO;
import com.example.SocialMediaApp.service.UpdateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class ProfileController {

    @Autowired
    private UpdateService updateService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDTO> getProfile() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserProfileDTO profile = updateService.getProfile(username);
        return ResponseEntity.ok(profile);
    }
//    @PutMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @Operation(summary = "Update user profile",
//            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
//                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
//                            schema = @Schema(implementation = UserProfileDTO.class))))
//    public ResponseEntity<UserProfileDTO> updateProfile(
////            @RequestPart("profile") UserProfileDTO profileDTO,
//            @ModelAttribute UserProfileDTO profileDTO,
//            @RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture) throws IOException {

//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        UserProfileDTO updatedProfile = updateService.updateProfile(username, profileDTO, profilePicture);
//        return ResponseEntity.ok(updatedProfile);
//    }
@PutMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<UserProfileDTO> updateProfile(
        @RequestPart("profile") String profileJson,
        @RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture) throws IOException {

    String username = SecurityContextHolder.getContext().getAuthentication().getName();

    // Manual conversion using ObjectMapper
    ObjectMapper objectMapper = new ObjectMapper();
    UserProfileDTO profileDTO = objectMapper.readValue(profileJson, UserProfileDTO.class);

    UserProfileDTO updatedProfile = updateService.updateProfile(username, profileDTO, profilePicture);
    return ResponseEntity.ok(updatedProfile);
}



    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> handleIOException(IOException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}