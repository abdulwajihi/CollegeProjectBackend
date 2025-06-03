package com.example.SocialMediaApp.service;

import com.example.SocialMediaApp.dto.User;
import com.example.SocialMediaApp.dto.UserDTO;
import com.example.SocialMediaApp.dto.UserImageDto;
import com.example.SocialMediaApp.repository.FollowRepository;
import com.example.SocialMediaApp.repository.ImageRepository;
import com.example.SocialMediaApp.repository.PreferenceRepository;
import com.example.SocialMediaApp.repository.SearchUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLOutput;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchUserService {
    @Autowired
    SearchUserRepository searchUserRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    FollowRepository followRepository;

    @Autowired
    LikeService likeService;

//    public List<UserDTO> getAllUsers() {
//        String currentUsername = getCurrentUsername();
//        List<User> users = searchUserRepository.findAll();
////        return users.stream().map(this::convertToDTO).collect(Collectors.toList());
//        return users.stream()
//                .filter(user -> !user.getUsername().equalsIgnoreCase(currentUsername)) // exclude self
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//    }
    @Transactional
public List<UserDTO> getAllUsers() {
    String currentEmail = getCurrentUserEmail();
    System.out.println("Authenticated email: " + currentEmail);

        if (currentEmail == null) return List.of();

        // Get the logged-in user
        User currentUser = searchUserRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new RuntimeException("Logged-in user not found"));

        Long currentUserId = currentUser.getId();

        // Get list of user IDs whom the current user is following
        List<Long> followingIds = followRepository.findFollowingIdsByFollowerId(currentUserId);
    List<User> users = searchUserRepository.findAll();

    return users.stream()
            .filter(user -> currentEmail == null || !user.getEmail().equalsIgnoreCase(currentEmail))
            .filter(user -> !followingIds.contains(user.getId()))            // exclude followed users
            .map(this::convertToDTO)
            .collect(Collectors.toList());
}



//}

//    public List<UserDTO> searchUsersByUsername(String username) {
//        String currentUsername = getCurrentUsername();
//        List<User> users = searchUserRepository.findByUsernameContainingIgnoreCase(username);
////        return users.stream().map(this::convertToDTO).collect(Collectors.toList());
//        return users.stream()
//                .filter(user -> !user.getUsername().equals(currentUsername)) // exclude self
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//
//    }

//    public List<User> searchUser(String query){
//        return searchUserRepository.searchUser(query);
//    }



//    private UserDTO convertToDTO(User user) {
//        return new UserDTO(
//                user.getId(),
//                user.getUsername(),
//                user.getEmail(),
//                user.getFirstName(),
//                user.getLastName(),
//                user.getProfilePictureUrl(),
//                user.getBio(),
//                followers,
//                followings,
//                totalImages
//        );

//    public List<UserDTO> searchUsersByUsername(String username) {
//        String currentUsername = getCurrentUsername();
//        List<User> users = searchUserRepository.findByUsernameContainingIgnoreCase(username);
//        return users.stream()
//                .filter(user -> !user.getUsername().equalsIgnoreCase(currentUsername)) // exclude self
//
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//    }
  @Transactional
public List<UserDTO> searchUsersByUsername(String username) {
    String currentUserEmail = getCurrentUserEmail();
    System.out.println("Current logged-in email: " + currentUserEmail);

    List<User> users = searchUserRepository.findByUsernameContainingIgnoreCase(username);
    users.forEach(user -> System.out.println("Found user: " + user.getUsername() + " | email: " + user.getEmail()));

    return users.stream()
            // exclude logged-in user based on email match
            .filter(user -> currentUserEmail == null || !user.getEmail().equalsIgnoreCase(currentUserEmail))
            .map(this::convertToDTO)
            .collect(Collectors.toList());
}


    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            System.out.println("No authentication found!");
            return null;
        }
        String email = authentication.getName();
        System.out.println("Authenticated email: " + email);
        return email;
    }



//    public List<User> searchUser(String query) {
//        return searchUserRepository.searchUser(query);
//    }

    private UserDTO convertToDTO(User user) {
        String currentEmail = getCurrentUserEmail();
        int followers = followRepository.countByFollowingId(user.getId());
        int following = followRepository.countByFollowerId(user.getId());



        List<UserImageDto> uploadedImages = imageRepository.findByUserId(user.getId())
                .stream()
                .map(img -> new UserImageDto(
                        img.getId(),
                        img.getFilePath(),
                        img.getUploadedAt(),
                        List.of(img.getTag()), // Confirm this is a single tag. If already List<String>, use directly.
                        img.getLikes().size(),
                        img.getLikes().stream()
                                .map(like -> like.getUser().getUsername())
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());

        List<String> likedByUsernames = imageRepository.findByUserId(user.getId()).stream()
                .flatMap(img -> img.getLikes().stream())
                .map(like -> like.getUser().getUsername())
                .distinct()
                .collect(Collectors.toList());

        Long userIdToReturn = (currentEmail != null && user.getEmail().equalsIgnoreCase(currentEmail))
                ? null : user.getId();



        return new UserDTO(
                userIdToReturn,
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getBio(),
                user.getProfilePictureUrl(),
                followers,
                following,
                uploadedImages,
                likedByUsernames

        );

    }
//    private String getCurrentUsername() {
//        String username= SecurityContextHolder.getContext().getAuthentication().getName();
//        System.out.println("Logged-in Username" +username);
//        return username;
//    }



//    private UserDTO convertToDTO(User user) {
//        long followers = followRepository.countByFollowingId(user.getId());
//        long followings = followRepository.countByFollowerId(user.getId());
//        long totalImages = imageRepository.countByUserId(user.getId());

    }


