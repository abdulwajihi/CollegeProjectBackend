package com.example.SocialMediaApp.dto;


import jakarta.persistence.*;
import lombok.Data;

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
    private User user;
    @OneToMany(mappedBy = "image", cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.EAGER)
    private List<PostLike> likes = new ArrayList<>();
    private long likeCount;




}
