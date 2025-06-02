package com.example.SocialMediaApp.repository;

import com.example.SocialMediaApp.dto.Follow;
import com.example.SocialMediaApp.dto.FollowId;
import com.example.SocialMediaApp.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, FollowId> {
    // Fetch users who follow the given user (followers)
    @Query("SELECT f.follower FROM Follow f WHERE f.following.id = :userId")
    List<User> findFollowers(@Param("userId") Long userId);

    // Fetch users whom the given user follows (following)
    @Query("SELECT f.following FROM Follow f WHERE f.follower.id = :userId")
    List<User> findFollowing(@Param("userId") Long userId);

    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);

    void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);

    List<Follow> findByFollowerId(Long followerId);

    List<Follow> findByFollowingId(Long followingId);
    List<Follow> findByFollower(User follower);

    int countByFollowerId(Long followerId);
    int countByFollowingId(Long followingId);

}