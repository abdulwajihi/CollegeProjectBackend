//package com.example.SocialMediaApp.service;
//
//import com.example.SocialMediaApp.dto.User;
//import com.example.SocialMediaApp.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Service
//public class CleanupService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Scheduled(fixedRate = 5 * 60 * 1000) // every 5 minutes
//    public void deleteStaleUnverifiedUsers() {
//        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(3);
//        List<User> staleUsers = userRepository.findByVerifiedFalseAndCreatedAtBefore(cutoff);
//        for (User user : staleUsers) {
//            userRepository.delete(user); // cascade preferences + OTP
//        }
//        System.out.println("🧹 Cleaned up " + staleUsers.size() + " stale unverified users.");
//    }
//}
//
package com.example.SocialMediaApp.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.SocialMediaApp.dto.User;
import com.example.SocialMediaApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CleanupService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Scheduled(fixedRate = 5 * 60 * 1000) // Every 5 minutes
    public void deleteStaleUnverifiedUsers() {
        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(3);
        List<User> staleUsers = userRepository.findByVerifiedFalseAndCreatedAtBefore(cutoff);

        for (User user : staleUsers) {
            try {
                String url = user.getProfilePictureUrl();
                if (url != null && url.contains("res.cloudinary.com")) {
                    String publicId = extractPublicIdFromUrl(url);
                    if (publicId != null) {
                        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                        System.out.println("🗑️ Deleted Cloudinary avatar: " + publicId);
                    }
                }
            } catch (Exception e) {
                System.err.println("⚠️ Failed to delete avatar for user: " + user.getUsername() + " - " + e.getMessage());
            }

            userRepository.delete(user); // Cascade preferences + OTP
        }

        System.out.println("🧹 Cleaned up " + staleUsers.size() + " stale unverified users.");
    }

    // Helper: Extract public_id from Cloudinary secure_url
    private String extractPublicIdFromUrl(String url) {
        try {
            URL u = new URL(url);
            String path = u.getPath(); // e.g. /your-cloud/image/upload/v123456789/avatars/username.png
            String[] parts = path.split("/upload/");
            if (parts.length == 2) {
                String remainingPath = parts[1];
                String[] segments = remainingPath.split("\\.");
                return segments[0]; // public_id without extension
            }
        } catch (Exception e) {
            System.err.println("❌ Invalid Cloudinary URL: " + url);
        }
        return null;
    }
}
