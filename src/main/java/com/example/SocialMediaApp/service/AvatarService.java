package com.example.SocialMediaApp.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Map;

@Service
public class AvatarService {

    @Autowired
    private Cloudinary cloudinary;

    public String generateInitialsAvatar(String firstName, String lastName, String username) {
        String initials = (firstName.charAt(0) + "" + lastName.charAt(0)).toUpperCase();

        String svg = "<svg xmlns='http://www.w3.org/2000/svg' width='256' height='256'>" +
                "<rect width='100%' height='100%' fill='#4A90E2'/>" +
                "<text x='50%' y='50%' dominant-baseline='middle' text-anchor='middle' " +
                "font-size='100' fill='white' font-family='Arial'>" +
                initials +
                "</text></svg>";

        try {
            // 1. Generate PNG image from SVG to temp file
            File tempFile = File.createTempFile(username + "_avatar", ".png");
            try (OutputStream os = new FileOutputStream(tempFile)) {
                TranscoderInput input = new TranscoderInput(new StringReader(svg));
                TranscoderOutput output = new TranscoderOutput(os);
                PNGTranscoder transcoder = new PNGTranscoder();
                transcoder.transcode(input, output);
            }

            // 2. Upload to Cloudinary
            Map uploadResult = cloudinary.uploader().upload(tempFile, ObjectUtils.asMap(
                    "public_id", "avatars/" + username, // folder + name in Cloudinary
                    "overwrite", true
            ));

            // 3. Clean up temp file
            tempFile.delete();

            // 4. Return secure Cloudinary URL
            return (String) uploadResult.get("secure_url");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Avatar generation/upload failed: " + e.getMessage());
        }
    }
}
