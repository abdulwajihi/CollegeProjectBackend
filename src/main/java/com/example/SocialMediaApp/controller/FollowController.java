package com.example.SocialMediaApp.controller;

import com.example.SocialMediaApp.dto.Follow;
import com.example.SocialMediaApp.dto.User;
import com.example.SocialMediaApp.dto.UserResponseDTO;
import com.example.SocialMediaApp.repository.FollowRepository;
import com.example.SocialMediaApp.repository.UserRepository;
import com.example.SocialMediaApp.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

//@RestController
//@RequestMapping("/api/follow")
//public class FollowController {
//
//    @Autowired
//    private FollowService followService;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private FollowRepository followRepository;
//
//    // ✅ Secure follow endpoint
//    @PostMapping("/{followingId}")
//    public ResponseEntity<?> followUser(@PathVariable Long followingId) {
//        try {
//            // 🔐 Get current logged-in user
//            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//            String email = auth.getName(); // or username
//            User follower = userRepository.findByEmail(email)
//                    .orElseThrow(() -> new RuntimeException("Logged-in user not found"));
//
//            // 🎯 Target user to be followed
//            User following = userRepository.findById(followingId)
//                    .orElseThrow(() -> new RuntimeException("User to follow not found"));
//
//            // ✅ Prevent duplicate follows
//            if (followRepository.existsByFollowerIdAndFollowingId(follower.getId(), following.getId())) {
//                return ResponseEntity.badRequest().body("You are already following this user.");
//            }
//
//            // Save follow relationship
//            Follow follow = new Follow();
//            follow.setFollower(follower);
//            follow.setFollowing(following);
//            followRepository.save(follow);
//
//            return ResponseEntity.ok("Followed successfully");
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Map.of("error", "Follow failed: " + e.getMessage()));
//        }
//    }
//
//    // ✅ Optional: Unfollow
//    @DeleteMapping("/{followingId}")
//    public ResponseEntity<?> unfollowUser(@PathVariable Long followingId) {
//        try {
//            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//            String email = auth.getName();
//            User follower = userRepository.findByEmail(email)
//                    .orElseThrow(() -> new RuntimeException("User not found"));
//
//            User following = userRepository.findById(followingId)
//                    .orElseThrow(() -> new RuntimeException("User to unfollow not found"));
//
//            followRepository.deleteByFollowerIdAndFollowingId(follower.getId(), followingId);
//            return ResponseEntity.ok("Unfollowed successfully");
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Map.of("error", "Unfollow failed: " + e.getMessage()));
//        }
//    }
//    @GetMapping("/{username}/followers")
//    public ResponseEntity<List<UserResponseDTO>> getFollowers(@PathVariable String username) {
//        return ResponseEntity.ok(followService.getFollowers(username));
//    }
//
//    @GetMapping("/{username}/following")
//    public ResponseEntity<List<UserResponseDTO>> getFollowings(@PathVariable String username) {
//        return ResponseEntity.ok(followService.getFollowings(username));
//    }
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
//        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler(IllegalStateException.class)
//    public ResponseEntity<String> handleIllegalStateException(IllegalStateException ex) {
//        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
//    }
//}
