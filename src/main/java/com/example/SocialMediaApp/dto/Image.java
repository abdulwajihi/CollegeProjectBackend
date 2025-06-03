package com.example.SocialMediaApp.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String filename;
    private String tag;
    private String filePath;
    @ManyToOne
    @JsonIgnoreProperties("images")  // Only if User has a list of images
    private User user;
    @OneToMany(mappedBy = "image", cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.EAGER)
    private List<PostLike> likes = new ArrayList<>();
    private long likeCount;
    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    @PrePersist
    protected void onCreate() {
        this.uploadedAt = LocalDateTime.now();
    }
}
