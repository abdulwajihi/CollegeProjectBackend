package com.example.SocialMediaApp.dto;

import com.example.SocialMediaApp.dto.Image;

import java.time.LocalDateTime;

public class ImageResponseDto {
    private Long id;
    private String filename;
    private String tag;
    private String imageUrl;
    private String username;
    private int likes;
    private boolean likedByUser;
    private long likeCount;


    public ImageResponseDto() {}

    // Full constructor with individual fields
    public ImageResponseDto(Long id, String filename, String tag, String imageUrl, String username, int likes,int likeCount) {
        this.id = id;
        this.filename = filename;
        this.tag = tag;
        this.imageUrl = imageUrl;
        this.username = username;
        this.likes = likes;
        this.likeCount=likeCount;
    }

    // ✅ Add this constructor for convenience
    public ImageResponseDto(Image image) {
        this.id = image.getId();
        this.filename = image.getFilename();
        this.tag = image.getTag();
        this.imageUrl = image.getFilePath();
        this.username = image.getUser().getUsername();
        this.likes = image.getLikes().size();
        this.likeCount=image.getLikeCount();
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

    public boolean isLikedByUser() {
        return likedByUser;
    }
    public void setLikedByUser(boolean likedByUser) {
        this.likedByUser = likedByUser;
    }

    public long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(long likeCount) {
        this.likeCount = likeCount;
    }
}
