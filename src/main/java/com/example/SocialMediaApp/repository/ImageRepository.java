package com.example.SocialMediaApp.repository;

import com.example.SocialMediaApp.dto.Image;
import com.example.SocialMediaApp.dto.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    // ✅ For feed: images uploaded by followed users and matching tags, sorted by number of likes
    @Query("SELECT i FROM Image i WHERE i.user IN :users AND i.tag IN :tags ORDER BY SIZE(i.likes) DESC")
    List<Image> findByUserInAndTagInOrderByLikesDesc(
            @Param("users") List<User> users,
            @Param("tags") List<String> tags
    );

    // ✅ For recommendations: images matching user preferences only, sorted by likes
    @Query("SELECT i FROM Image i WHERE i.tag IN :tags ORDER BY SIZE(i.likes) DESC")
    List<Image> findByTagInOrderByLikesDesc(@Param("tags") List<String> tags);

    // ✅ Get all images posted by a specific user
    @Query("SELECT i FROM Image i LEFT JOIN FETCH i.likes LEFT JOIN FETCH i.user WHERE i.user.username = :username")
    List<Image> findAllImagesByUsernameWithLikes(@Param("username") String username);


    // Optional: find images by a user and tag (if still needed)
    List<Image> findByTagAndUserId(String tag, Long userId);
    long countByUserId(Long userId);
    List<Image> findByUserId(Long userId);


}
