package com.example.SocialMediaApp.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Service
public class AvatarService {

    @Autowired
    private Cloudinary cloudinary;

    private static final String[] COLORS = {
            "#FFB6C1", "#FF7F50", "#87CEFA", "#98FB98", "#DDA0DD",
            "#FFD700", "#40E0D0", "#FA8072", "#ADD8E6", "#90EE90"
    };

    private String getRandomColor() {
        Random random = new Random();
        return COLORS[random.nextInt(COLORS.length)];
    }

    private String getInitials(String firstName, String lastName) {
        String first = (firstName != null && !firstName.isEmpty()) ? firstName.substring(0, 1).toUpperCase() : "";
        String last = (lastName != null && !lastName.isEmpty()) ? lastName.substring(0, 1).toUpperCase() : "";
        return first + last;
    }

//    @Async
    public CompletableFuture<String> generateInitialsAvatar(String firstName, String lastName, String username) {
        String initials = getInitials(firstName, lastName);
        String bgColor = getRandomColor(); // 🎨 Soft background

        String svg = "<svg xmlns='http://www.w3.org/2000/svg' width='256' height='256'>" +
                "<rect width='100%' height='100%' fill='" + bgColor + "'/>" +
                "<text x='50%' y='50%' dy='.35em' text-anchor='middle' " +
                "font-size='100' fill='white' font-family='Arial'>" +
                initials +
                "</text></svg>";

        try {
            File tempFile = File.createTempFile(username + "_avatar", ".png");

            try (OutputStream os = new FileOutputStream(tempFile)) {
                TranscoderInput input = new TranscoderInput(new StringReader(svg));
                TranscoderOutput output = new TranscoderOutput(os);
                PNGTranscoder transcoder = new PNGTranscoder();
                transcoder.transcode(input, output);
            }

            Map uploadResult = cloudinary.uploader().upload(tempFile, ObjectUtils.asMap(
                    "public_id", "avatars/" + username,
                    "overwrite", true
            ));

            tempFile.delete();

            String url = (String) uploadResult.get("secure_url");
            return CompletableFuture.completedFuture(url);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Avatar generation/upload failed: " + e.getMessage());
        }
    }
}
