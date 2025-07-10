package com.example.SocialMediaApp.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.SocialMediaApp.dto.Follow;
import com.example.SocialMediaApp.dto.Image;
import com.example.SocialMediaApp.dto.User;
import com.example.SocialMediaApp.dto.UserPreference;
import com.example.SocialMediaApp.repository.FollowRepository;
import com.example.SocialMediaApp.repository.ImageRepository;
import com.example.SocialMediaApp.repository.PreferenceRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final Cloudinary cloudinary;
    private final FollowRepository followRepository;
    private final PreferenceRepository userPreferenceRepository;

    public ImageService(ImageRepository imageRepository,
                        Cloudinary cloudinary,
                        FollowRepository followRepository,
                        PreferenceRepository userPreferenceRepository) {
        this.imageRepository = imageRepository;
        this.cloudinary = cloudinary;
        this.followRepository = followRepository;
        this.userPreferenceRepository = userPreferenceRepository;
    }

    // Uploads the image to Cloudinary and saves metadata in DB.
    public Image uploadImage(MultipartFile file, String tag, User user) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        String cloudinaryUrl = (String) uploadResult.get("secure_url");

        Image image = new Image();
        image.setFilename(file.getOriginalFilename());
        image.setTag(tag != null ? tag : "untagged");
        image.setFilePath(cloudinaryUrl);
        image.setUser(user);

        return imageRepository.save(image);
    }

    public String getImageUrl(Long id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found with id: " + id));
        return image.getFilePath();
    }

//    // Personalized feed: followed users + matching preferences
//    public List<Image> getFeedForUser(User user) {
//        List<Follow> followingList = followRepository.findByFollower(user);
//        List<User> followingUsers = followingList.stream()
//                .map(Follow::getFollowing)
//                .collect(Collectors.toList());
//
//        if (followingUsers.isEmpty()) {
//            return Collections.emptyList();
//        }
//
//        List<UserPreference> preferences = userPreferenceRepository.findByUser(user);
//        List<String> preferenceTags = preferences.stream()
//                .map(UserPreference::getPreferenceName)
//                .collect(Collectors.toList());
//
//        if (preferenceTags.isEmpty()) {
//            return Collections.emptyList();
//        }
//
//        return imageRepository.findByUserInAndTagInOrderByLikesDesc(followingUsers, preferenceTags);
//    }
    @Transactional(readOnly = true)
    public List<Image> getFeedForUser(User user) {
        List<Follow> followingList = followRepository.findByFollower(user);
        List<User> followingUsers = followingList.stream()
                .map(Follow::getFollowing)
                .filter(u -> !u.getId().equals(user.getId())) // 👈 Exclude self
                .collect(Collectors.toList());

        if (followingUsers.isEmpty()) {
            return Collections.emptyList();
        }

        List<UserPreference> preferences = userPreferenceRepository.findByUser(user);
        List<String> preferenceTags = preferences.stream()
                .map(UserPreference::getPreferenceName)
                .collect(Collectors.toList());

        if (preferenceTags.isEmpty()) {
            return Collections.emptyList();
        }

//        List<Image> images = imageRepository.findByUserInAndTagInOrderByLikesDesc(followingUsers, preferenceTags);
//
//        // Optional: Double-check and exclude images uploaded by the current user just in case
//        return images.stream()
//                .filter(img -> !img.getUser().getId().equals(user.getId()))
//                .collect(Collectors.toList());
    Pageable pageable = PageRequest.of(0, 20); // Limit feed to 20 images
    return imageRepository.findByUserInAndTagInOrderByLikesDesc(followingUsers, preferenceTags, pageable)
            .stream()
            .filter(img -> !img.getUser().getId().equals(user.getId()))
            .collect(Collectors.toList());
    }


    // 🔄 New: Recommendation based only on user preferences, ordered by likes descending
    @Transactional(readOnly = true)
    public List<Image> getRecommendations(User user) {
        List<UserPreference> preferences = userPreferenceRepository.findByUser(user);
        List<String> preferenceTags = preferences.stream()
                .map(UserPreference::getPreferenceName)
                .collect(Collectors.toList());

        if (preferenceTags.isEmpty()) {
            return Collections.emptyList();
        }

//        return imageRepository.findByTagInOrderByLikesDesc(preferenceTags)
//                .stream()
//                .filter(image -> !image.getUser().getId().equals(user.getId()))
//                .collect(Collectors.toList());
        Pageable pageable = PageRequest.of(0, 20); // Limit recommendations to 20 images
        return imageRepository.findByTagInOrderByLikesDesc(preferenceTags, pageable)
                .stream()
                .filter(image -> !image.getUser().getId().equals(user.getId()))
                .collect(Collectors.toList());
    }
}
