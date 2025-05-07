//package com.example.SocialMediaApp.repository;
//
//import com.example.SocialMediaApp.dto.Image;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.List;
//
//public interface ImageRepository extends JpaRepository<Image, Long> {
//    List<Image> findByTag(String tag);
//    List<Image> findByUserId(Long userId);
//    List<Image> findByTagAndUserId(String tag, Long userId); // Add this method
//}
