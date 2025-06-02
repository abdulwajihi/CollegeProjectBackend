package com.example.SocialMediaApp.dto;

import com.example.SocialMediaApp.dto.Image;

import java.time.LocalDateTime;

public class ImageResponseDto {
    private Long userId;
    private Long id;
    private String filename;
    private String tag;
    private String imageUrl;
    private String username;
    private int likes;
    private long likeCount;
    private LocalDateTime uploadedAt;



    public ImageResponseDto() {}




    // Full constructor with individual fields
    public ImageResponseDto(Long userId,Long id, String filename, String tag, String imageUrl, String username, int likes,int likeCount,LocalDateTime uploadedAt) {
        this.userId=userId;
        this.id = id;
        this.filename = filename;
        this.tag = tag;
        this.imageUrl = imageUrl;
        this.username = username;
        this.likes = likes;
        this.likeCount=likeCount;
        this.uploadedAt=uploadedAt;
    }

    // ✅ Add this constructor for convenience
    public ImageResponseDto(Image image) {
        this.userId=image.getUser().getId();
        this.id = image.getId();
        this.filename = image.getFilename();
        this.tag = image.getTag();
        this.imageUrl = image.getFilePath();
        this.username = image.getUser().getUsername();
        this.likes = image.getLikes().size();
        this.likeCount=image.getLikeCount();
        this.uploadedAt=image.getUploadedAt();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }

    public String getTag() { return tag; }
    public void setTag(String tag) { this.tag = tag; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public int getLikes() { return likes; }
    public void setLikes(int likes) { this.likes = likes; }



    public long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(long likeCount) {
        this.likeCount = likeCount;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
