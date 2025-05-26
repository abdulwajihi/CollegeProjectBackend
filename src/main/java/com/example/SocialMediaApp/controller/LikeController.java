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

