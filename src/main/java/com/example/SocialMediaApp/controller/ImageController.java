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
import org.springframework.security.core.context.SecurityContextHolder;
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
                return dto;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch recommendations: " + e.getMessage()));
        }
    }

    // 🔍 Get image URL by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getImageUrl(@PathVariable Long id) {
        String imageUrl = imageService.getImageUrl(id);
        return ResponseEntity.ok().body(imageUrl);
    }

    // 🧠 Get personalized feed (followed users + preferences)
    // ✅ Personalized feed using logged-in user
    @GetMapping("/feed")
    public ResponseEntity<List<ImageResponseDto>> getPersonalizedFeed() {
        User user = getAuthenticatedUser();

        List<Image> feedImages = imageService.getFeedForUser(user);
        List<ImageResponseDto> response = feedImages.stream()
                .map(img -> new ImageResponseDto(
                        img.getUser().getId(),
                        img.getId(),
                        img.getFilename(),
                        img.getTag(),
                        img.getFilePath(),
                        img.getUser().getUsername(),
                        img.getLikes().size(),
                        (int) img.getLikeCount(),
                        img.getUploadedAt()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // ✅ Utility method to get authenticated user
//    private User getAuthenticatedUser() {
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        return userRepository.findByUsername(username)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//    }
    private User getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("Extracted email from token: " + email);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
    }



}

//package com.example.SocialMediaApp.controller;
//
//import com.example.SocialMediaApp.dto.Image;
//import com.example.SocialMediaApp.dto.ImageResponseDto;
//import com.example.SocialMediaApp.dto.User;
//import com.example.SocialMediaApp.repository.UserRepository;
//import com.example.SocialMediaApp.service.ImageService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@RestController
//@RequestMapping("/api/images")
//public class ImageController {
//
//    @Autowired
//    private ImageService imageService;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    // ✅ Upload image securely using authenticated user
//    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<?> uploadImage(
//            @RequestParam("image") MultipartFile file,
//            @RequestParam(value = "tag", required = false) String tag) {
//
//        try {
//            // 🔐 Get logged-in user from security context
//            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//            String email = auth.getName(); // or get username if you use username login
//
//            User user = userRepository.findByEmail(email)
//                    .orElseThrow(() -> new RuntimeException("User not found"));
//
//            Image uploadedImage = imageService.uploadImage(file, tag, user);
//            ImageResponseDto responseDto = new ImageResponseDto(uploadedImage);
//
//            return ResponseEntity.ok(Map.of(
//                    "message", "Image uploaded successfully",
//                    "image", responseDto
//            ));
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Map.of("error", "Upload failed: " + e.getMessage()));
//        }
//    }
//
//    // ✅ Secure recommendation API using authenticated user
//    @GetMapping("/recommend")
//    public ResponseEntity<?> getRecommendedImages() {
//        try {
//            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//            String email = auth.getName();
//
//            User user = userRepository.findByEmail(email)
//                    .orElseThrow(() -> new RuntimeException("User not found"));
//
//            List<Image> images = imageService.getRecommendations(user);
//
//            List<ImageResponseDto> response = images.stream()
//                    .map(ImageResponseDto::new)
//                    .collect(Collectors.toList());
//
//            return ResponseEntity.ok(response);
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Map.of("error", "Failed to fetch recommendations: " + e.getMessage()));
//        }
//    }
//}
