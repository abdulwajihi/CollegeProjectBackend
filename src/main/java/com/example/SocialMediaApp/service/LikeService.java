//package com.example.SocialMediaApp.service;
//
//import com.example.SocialMediaApp.dto.Image;
//import com.example.SocialMediaApp.dto.PostLike;
//import com.example.SocialMediaApp.dto.User;
//import com.example.SocialMediaApp.repository.ImageRepository;
//import com.example.SocialMediaApp.repository.LikeRepository;
//import com.example.SocialMediaApp.repository.UserRepository;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Service
//public class LikeService {
//
//    private final LikeRepository likeRepository;
//    private final ImageRepository imageRepository;
//    private final UserRepository userRepository;
//
//    public LikeService(LikeRepository likeRepository, ImageRepository imageRepository, UserRepository userRepository) {
//        this.likeRepository = likeRepository;
//        this.imageRepository = imageRepository;
//        this.userRepository = userRepository;
//    }
//
//    public String likeImage(Long userId, Long imageId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Image image = imageRepository.findById(imageId)
//                .orElseThrow(() -> new RuntimeException("Image not found"));
//
//        if (likeRepository.existsByUserAndImage(user, image)) {
//            return "Already liked";
//        }
//
//        PostLike like = new PostLike();
//        like.setUser(user);
//        like.setImage(image);
//        likeRepository.save(like);
//
////        // Update like count (if you're storing it)
////        image.setLikes(likeRepository.countByImage(image));
////        imageRepository.save(image);
//        // ✅ Update like count
//        long count = likeRepository.countByImage(image);
//        image.setLikeCount(count);
//        imageRepository.save(image);
//        return "Liked successfully";
//    }
//    @Transactional
//    public String unlikeImage(Long userId, Long imageId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Image image = imageRepository.findById(imageId)
//                .orElseThrow(() -> new RuntimeException("Image not found"));
//
//        PostLike like = likeRepository.findByUserAndImage(user, image)
//                .orElseThrow(() -> new RuntimeException("Like not found"));
//
//
//        likeRepository.deleteById(like.getId());
//        // Update like count
////        image.setLikes(likeRepository.countByImage(image));
////        imageRepository.save(image);
//        // ✅ Update like count
//        long count = likeRepository.countByImage(image);
//        image.setLikeCount(count);
//        imageRepository.save(image);
//        return "Unliked successfully";
//    }
//
////    public List<PostLike> getLikesCount(Image image) {
////        return likeRepository.countByImage(image);
////    }
//public long getLikesCount(Image image) {
//    return likeRepository.countByImage(image);
//}
//
//}
//
package com.example.SocialMediaApp.service;

import com.example.SocialMediaApp.dto.Image;
import com.example.SocialMediaApp.dto.PostLike;
import com.example.SocialMediaApp.dto.User;
import com.example.SocialMediaApp.repository.ImageRepository;
import com.example.SocialMediaApp.repository.LikeRepository;
import com.example.SocialMediaApp.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;

    public LikeService(LikeRepository likeRepository, ImageRepository imageRepository, UserRepository userRepository) {
        this.likeRepository = likeRepository;
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
    }

    public String likeImage(Long userId, Long imageId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        if (likeRepository.existsByUserAndImage(user, image)) {
            return "Already liked";
        }

        PostLike like = new PostLike();
        like.setUser(user);
        like.setImage(image);
        likeRepository.save(like);

        long count = likeRepository.countByImage(image);
        image.setLikeCount(count);
        imageRepository.save(image);
        return "Liked successfully";
    }

    @Transactional
    public String unlikeImage(Long userId, Long imageId) {
        System.out.println("Attempting to unlike imageId=" + imageId + " by userId=" + userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        // Check first
        if (!likeRepository.existsByUserAndImage(user, image)) {
            return "Like not found";
        }

//        PostLike like = likeRepository.findByUserAndImage(user, image)
//                .orElse(null);
//
//        if (like == null) {
//            return "Like not found";
//        }
//
//        System.out.println("Found like to delete: likeId=" + like.getId());

//        // Delete by ID to avoid any session issues
//        likeRepository.deleteById(like.getId());
        // ✅ Use direct delete
        likeRepository.deleteByUserAndImage(user, image);

        // Update like count
        long count = likeRepository.countByImage(image);
        image.setLikeCount(count);
        imageRepository.save(image);

        return "Unliked successfully";
    }

    public long getLikesCount(Image image) {
        return likeRepository.countByImage(image);
    }
}
