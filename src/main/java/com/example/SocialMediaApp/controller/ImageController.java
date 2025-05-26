package com.example.SocialMediaApp.controller;

import com.example.SocialMediaApp.dto.Image;
import com.example.SocialMediaApp.dto.ImageResponseDto;
import com.example.SocialMediaApp.dto.User;
import com.example.SocialMediaApp.repository.UserRepository;
import com.example.SocialMediaApp.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserRepository userRepository;

    // Only the logged-in user should be allowed to post images.
    // Here, we are still using a userId parameter for simplicity.
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(
            @RequestParam("image") MultipartFile file,
            @RequestParam("tag") String tag,
            @RequestParam("userId") Long userId) {

        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Image uploadedImage = imageService.uploadImage(file, tag, user);
            ImageResponseDto responseDto = new ImageResponseDto(uploadedImage);

            return ResponseEntity.ok(Map.of(
                    "message", "Image uploaded successfully",
                    "image", responseDto
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Upload failed: " + e.getMessage()));
        }
    }

    @GetMapping("/recommend")
    public ResponseEntity<?> getRecommendedImages(@RequestParam("userId") Long userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Call the updated method with only the user object
            List<Image> images = imageService.getRecommendations(user);

            List<ImageResponseDto> response = images.stream().map(image -> {
                ImageResponseDto dto = new ImageResponseDto(image);
                dto.setLikedByUser(image.getLikes().contains(user));
                return dto;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch recommendations: " + e.getMessage()));
        }
    }


}
