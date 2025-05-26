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
import org.springframework.stereotype.Service;
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

    // Personalized feed: followed users + matching preferences
    public List<Image> getFeedForUser(User user) {
        List<Follow> followingList = followRepository.findByFollower(user);
        List<User> followingUsers = followingList.stream()
                .map(Follow::getFollowing)
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

        return imageRepository.findByUserInAndTagInOrderByLikesDesc(followingUsers, preferenceTags);
    }

    // 🔄 New: Recommendation based only on user preferences, ordered by likes descending
    public List<Image> getRecommendations(User user) {
        List<UserPreference> preferences = userPreferenceRepository.findByUser(user);
        List<String> preferenceTags = preferences.stream()
                .map(UserPreference::getPreferenceName)
                .collect(Collectors.toList());

        if (preferenceTags.isEmpty()) {
            return Collections.emptyList();
        }

        return imageRepository.findByTagInOrderByLikesDesc(preferenceTags);
    }
}
