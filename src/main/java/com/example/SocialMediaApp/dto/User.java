package com.example.SocialMediaApp.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true,nullable = false)
    private String username;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    private String gender;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private String phoneNumber;
    private boolean verified; // Initial signup verification
    private int tokenVersion;
    @Version
    private Long version; // Added for optimistic locking
    @Column(length = 255)
    private String preferenceNames;
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<UserPreference> preferences = new ArrayList<>();

//    @OneToMany(mappedBy = "follower")
//    @JsonIgnore
//    private List<Integer> following;
//
//    @OneToMany(mappedBy = "following")
//    @JsonIgnore
//    private List<Integer> followers;
@JsonIgnore
@OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
private List<Follow> following = new ArrayList<>(); // Fixed to List<Follow>

    @JsonIgnore
    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Follow> followers = new ArrayList<>(); // Fixed to List<Follow>


}