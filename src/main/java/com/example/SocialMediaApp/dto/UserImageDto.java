package com.example.SocialMediaApp.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserImageDto {//get all details of all images for User Profile
    private Long id;
    private String imageUrl;
//    private String caption;
    private LocalDateTime uploadedAt;
    private List<String> tags;
    private int likeCount;                         // ✅ Total likes
    private List<String> likedByUsernames;

    public UserImageDto(Long id, String imageUrl, LocalDateTime uploadedAt, List<String> tags, int likeCount, List<String> likedByUsernames) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.uploadedAt = uploadedAt;
        this.tags = tags;
        this.likeCount = likeCount;
        this.likedByUsernames = likedByUsernames;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public List<String> getLikedByUsernames() {
        return likedByUsernames;
    }

    public void setLikedByUsernames(List<String> likedByUsernames) {
        this.likedByUsernames = likedByUsernames;
    }
}
