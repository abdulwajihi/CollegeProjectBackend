package com.example.SocialMediaApp.service;

import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class AvatarService {

    public String generateInitialsAvatar(String firstName, String lastName, String username) throws Exception {
        String initials = (firstName.charAt(0) + "" + lastName.charAt(0)).toUpperCase();

        // SVG template with initials
        String svg = "<svg xmlns='http://www.w3.org/2000/svg' width='256' height='256'>" +
                "<rect width='100%' height='100%' fill='#4A90E2'/>" +
                "<text x='50%' y='50%' dominant-baseline='middle' text-anchor='middle' " +
                "font-size='100' fill='white' font-family='Arial'>" +
                initials +
                "</text></svg>";

        // Output path to static resources
        String staticFolderPath = "src/main/resources/static/avatars/";
        new File(staticFolderPath).mkdirs(); // create folder if not exist
        String outputFilename = username + "_avatar.png";
        String outputPath = staticFolderPath + outputFilename;

        try (OutputStream os = new FileOutputStream(outputPath)) {
            TranscoderInput input = new TranscoderInput(new StringReader(svg));
            TranscoderOutput output = new TranscoderOutput(os);
            PNGTranscoder transcoder = new PNGTranscoder();
            transcoder.transcode(input, output);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to generate avatar image");
        }

        // Final image URL that frontend can access
        return "/avatars/" + outputFilename;
    }
}
