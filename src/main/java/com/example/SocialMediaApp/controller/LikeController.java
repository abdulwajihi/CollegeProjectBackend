package com.example.SocialMediaApp.controller;

import com.example.SocialMediaApp.service.LikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/like")
    public ResponseEntity<?> likeImage(@RequestParam Long userId, @RequestParam Long imageId) {
        String result = likeService.likeImage(userId, imageId);
        return ResponseEntity.ok(Map.of("message", result));
    }

    @PostMapping("/unlike")
    public ResponseEntity<?> unlikeImage(@RequestParam Long userId, @RequestParam Long imageId) {
        String result = likeService.unlikeImage(userId, imageId);
        return ResponseEntity.ok(Map.of("message", result));
    }
}


//package com.example.SocialMediaApp.controller;
//
//import com.example.SocialMediaApp.dto.User;
//import com.example.SocialMediaApp.repository.UserRepository;
//import com.example.SocialMediaApp.service.LikeService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/likes")
//public class LikeController {
//
//    private final LikeService likeService;
//    private final UserRepository userRepository;
//
//    public LikeController(LikeService likeService, UserRepository userRepository) {
//        this.likeService = likeService;
//        this.userRepository = userRepository;
//    }
//
//    @PostMapping("/like")
//    public ResponseEntity<?> likeImage(@RequestParam Long imageId) {
//        // 🔐 Get logged-in user from Security Context
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String email = auth.getName();
//
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("Logged-in user not found"));
//
//        String result = likeService.likeImage(user.getId(), imageId);
//        return ResponseEntity.ok(Map.of("message", result));
//    }
//
//    @PostMapping("/unlike")
//    public ResponseEntity<?> unlikeImage(@RequestParam Long imageId) {
//        // 🔐 Get logged-in user from Security Context
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String email = auth.getName();
//
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("Logged-in user not found"));
//
//        String result = likeService.unlikeImage(user.getId(), imageId);
//        return ResponseEntity.ok(Map.of("message", result));
//    }
//}
