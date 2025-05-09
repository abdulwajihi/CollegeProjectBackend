package com.example.SocialMediaApp.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "followers")
@IdClass(FollowId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Follow {
    @Id
    private Long followerId;
    @Id
    private Long followingId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followerId",referencedColumnName = "id",insertable = false,updatable = false)
    private User follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followingId", referencedColumnName = "id", insertable = false, updatable = false)
    private User following;

    public Follow(Long followerId, Long followingId) {
        this.followerId = followerId;
        this.followingId = followingId;
    }
}
