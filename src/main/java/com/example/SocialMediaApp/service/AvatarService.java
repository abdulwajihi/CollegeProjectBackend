//package com.example.SocialMediaApp.service;
//
//import com.example.SocialMediaApp.util.AvatarGenerator;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//
//@Service
//public class AvatarService {
//
//    @Autowired
//    private CloudinaryService cloudinaryService;  // आपका Cloudinary wrapper
//
//    /**
//     * @param initials first+last name initials
//     * @param username फ़ाइल का नाम बनाने के लिए
//     * @return Cloudinary URL
//     */
//    public String generateAndUploadAvatar(String initials, String username) {
//        try {
//            byte[] bytes = AvatarGenerator.generateAvatar(initials);
//            MultipartFile file = new MockMultipartFile(
//                    "file",
//                    username + "_avatar.png",
//                    "image/png",
//                    bytes
//            );
//            return cloudinaryService.uploadFile(file);
//        } catch (IOException e) {
//            throw new RuntimeException("Avatar generation failed: " + e.getMessage(), e);
//        }
//    }
//}