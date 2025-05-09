package com.example.SocialMediaApp.controller;

import com.example.SocialMediaApp.dto.UserResponseDTO;
import com.example.SocialMediaApp.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/follow")
public class FollowController {
    @Autowired
    FollowService followService;

    @PostMapping("/{followerUsername}/follow/{followingUsername}")
    public ResponseEntity<String> follow(@PathVariable String followerUsername, @PathVariable String followingUsername) {
        followService.followUser(followerUsername, followingUsername);
        return ResponseEntity.ok("Followed Successfully");
    }

    @DeleteMapping("/{followerUsername}/unfollow/{followingUsername}")
    public ResponseEntity<String> unfollow(@PathVariable String followerUsername, @PathVariable String followingUsername) {
        followService.unfollowUser(followerUsername, followingUsername);
        return ResponseEntity.ok("Unfollowed Successfully");
    }

    @GetMapping("/{username}/followers")
    public ResponseEntity<List<UserResponseDTO>> getFollowers(@PathVariable String username) {
        return ResponseEntity.ok(followService.getFollowers(username));
    }

    @GetMapping("/{username}/following")
    public ResponseEntity<List<UserResponseDTO>> getFollowings(@PathVariable String username) {
        return ResponseEntity.ok(followService.getFollowings(username));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}