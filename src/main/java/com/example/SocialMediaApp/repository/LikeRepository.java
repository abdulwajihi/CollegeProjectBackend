//package com.example.SocialMediaApp.repository;
//
//import com.example.SocialMediaApp.dto.Image;
//import com.example.SocialMediaApp.dto.PostLike;
//import com.example.SocialMediaApp.dto.User;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.List;
//import java.util.Optional;
//
//public interface LikeRepository extends JpaRepository<PostLike, Long> {
//    List<PostLike> countByImage(Image image); // Likes count for an image
//
//    boolean existsByUserAndImage(User user, Image image); // Check if user already liked
//
//    Optional<PostLike> findByUserAndImage(User user, Image image); // To unlike if needed
//}
package com.example.SocialMediaApp.repository;

import com.example.SocialMediaApp.dto.Image;
import com.example.SocialMediaApp.dto.PostLike;
import com.example.SocialMediaApp.dto.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<PostLike, Long> {
    long countByImage(Image image); // ✅ Fixed

    boolean existsByUserAndImage(User user, Image image); // ✅ Fine

    Optional<PostLike> findByUserAndImage(User user, Image image); // ✅ Fine

    @Modifying
    @Transactional
    @Query("DELETE FROM PostLike pl WHERE pl.user = :user AND pl.image = :image")
    void deleteByUserAndImage(@Param("user") User user, @Param("image") Image image);

}
