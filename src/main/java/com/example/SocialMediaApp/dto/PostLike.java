package com.example.SocialMediaApp.dto;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

//@Entity
//@Data
//public class PostLike {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    private User user;   // Jo like kar raha hai
//
//    @ManyToOne
//    private Image image; // Kis image ko like kiya
//
//    private LocalDateTime likedAt = LocalDateTime.now();
//}
//
@Entity
@Data
public class PostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id", nullable = false)
    private Image image;

    private LocalDateTime likedAt = LocalDateTime.now();

    // Getters and setters
}
