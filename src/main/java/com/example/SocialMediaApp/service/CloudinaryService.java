package com.example.SocialMediaApp.service;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.cloudinary.utils.ObjectUtils;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    public String uploadProfilePicture(MultipartFile file, Long userId) throws IOException {
        try {
            Map<String, Object> uploadParams = new HashMap<>();
            uploadParams.put("public_id", "profile_picture_user_" + userId);
            uploadParams.put("overwrite", true);

            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadParams);
            return uploadResult.get("url").toString();
        } catch (Exception e) {
            throw new IOException("Failed to upload profile picture to Cloudinary", e);
        }
    }
}