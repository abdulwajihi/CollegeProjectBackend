package com.example.SocialMediaApp.service;

import com.example.SocialMediaApp.dto.Follow;
import com.example.SocialMediaApp.dto.User;
import com.example.SocialMediaApp.dto.UserResponseDTO;
import com.example.SocialMediaApp.repository.FollowRepository;
import com.example.SocialMediaApp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FollowService {
    private static final Logger logger = LoggerFactory.getLogger(FollowService.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    FollowRepository followRepository;

    @Transactional
    public void followUser(String followerUsername, String followingUsername) {
        logger.info("Attempting to follow: {} -> {}", followerUsername, followingUsername);

        if (followerUsername.equals(followingUsername)) {
            logger.warn("User {} attempted to follow themselves", followerUsername);
            throw new IllegalArgumentException("You cannot follow yourself.");
        }
        User follower = userRepository.findByUsername(followerUsername)
                .orElseThrow(() -> new IllegalArgumentException("Follower not found: " + followerUsername));
        User following = userRepository.findByUsername(followingUsername)
                .orElseThrow(() -> new IllegalArgumentException("User to follow not found: " + followingUsername));
        if (!followRepository.existsByFollowerIdAndFollowingId(follower.getId(), following.getId())) {
            followRepository.save(new Follow(follower.getId(), following.getId()));
            logger.info("Successfully followed: {} -> {}", followerUsername, followingUsername);
        } else {
            logger.warn("Follow relationship already exists: {} -> {}", followerUsername, followingUsername);
        }
    }

    @Transactional
    public void unfollowUser(String followerUsername, String followingUsername) {
        logger.info("Attempting to unfollow: {} -> {}", followerUsername, followingUsername);
        User follower = userRepository.findByUsername(followerUsername)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + followerUsername));
        User following = userRepository.findByUsername(followingUsername)
                .orElseThrow(() -> new IllegalArgumentException("User to unfollow not found: " + followingUsername));

        Long followerId = follower.getId();
        Long followingId = following.getId();

        if (!followRepository.existsByFollowerIdAndFollowingId(followerId, followingId)) {
            logger.warn("Follow relationship does not exist: {} -> {}", followerUsername, followingUsername);
            throw new IllegalStateException("You are not following this user: " + followingUsername);
        }

        followRepository.deleteByFollowerIdAndFollowingId(followerId, followingId);
        logger.info("Successfully unfollowed: {} -> {}", followerUsername, followingUsername);
    }

    public List<UserResponseDTO> getFollowers(String username) {
        logger.info("Fetching followers for user: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
        List<User> followers = followRepository.findFollowers(user.getId());
        return followers.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<UserResponseDTO> getFollowings(String username) {
        logger.info("Fetching followings for user: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
        List<User> followings = followRepository.findFollowing(user.getId());
        return followings.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private UserResponseDTO convertToDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setProfilePictureUrl(user.getProfilePictureUrl());
        return dto;
    }
}