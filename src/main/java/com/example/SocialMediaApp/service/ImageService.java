//package com.example.SocialMediaApp.service;
//
//import com.example.SocialMediaApp.dto.Image;
//import com.example.SocialMediaApp.dto.User;
//import com.example.SocialMediaApp.repository.ImageRepository;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.List;
//import java.util.UUID;
//
//@Service
//public class ImageService {
//
//    private final ImageRepository imageRepository;
//
//    @Value("${spring.servlet.multipart.max-file-size:10MB}")
//    private String maxFileSize;
//
//    private final String uploadDir = "src/main/resources/uploads/";
//
//    public ImageService(ImageRepository imageRepository) {
//        this.imageRepository = imageRepository;
//    }
//
//    public Image uploadImage(MultipartFile file, String tag, User user) throws IOException {
//        File directory = new File(uploadDir);
//        if (!directory.exists()) {
//            directory.mkdirs();
//        }
//
//        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
//        Path filePath = Paths.get(uploadDir + filename);
//        Files.write(filePath, file.getBytes());
//
//        Image image = new Image();
//        image.setFilename(filename);
//        image.setTag(tag != null ? tag : "untagged");
//        image.setFilePath(filePath.toString());
//        image.setUser(user);
//        return imageRepository.save(image);
//    }
//
//    public byte[] getImage(Long id) throws IOException {
//        Image image = imageRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Image not found with id: " + id));
//        return Files.readAllBytes(Paths.get(image.getFilePath()));
//    }
//
//    public List<Image> getRecommendations(String tag, Long userId) {
//        return imageRepository.findByTagAndUserId(tag, userId);
//    }
//}
