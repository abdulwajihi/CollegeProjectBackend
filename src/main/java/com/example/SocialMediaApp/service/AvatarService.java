package com.example.SocialMediaApp.service;

import com.example.SocialMediaApp.util.AvatarGenerator;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class AvatarService {

    @Autowired
    private CloudinaryService cloudinaryService;

    public String generateAndUploadAvatar(String initials, String username) throws IOException {
        byte[] avatarBytes = AvatarGenerator.generateAvatar(initials);

        MultipartFile file = new MockMultipartFile(
                "avatar",
                username + "_avatar.png",
                "image/png",
                avatarBytes
        );

        return cloudinaryService.uploadFile(file);
    }
}
