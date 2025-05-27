package com.example.SocialMediaApp.service;

import com.example.SocialMediaApp.dto.User;
import com.example.SocialMediaApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CleanupService {

    @Autowired
    private UserRepository userRepository;

    @Scheduled(fixedRate = 5 * 60 * 1000) // every 5 minutes
    public void deleteStaleUnverifiedUsers() {
        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(3);
        List<User> staleUsers = userRepository.findByVerifiedFalseAndCreatedAtBefore(cutoff);
        for (User user : staleUsers) {
            userRepository.delete(user); // cascade preferences + OTP
        }
        System.out.println("🧹 Cleaned up " + staleUsers.size() + " stale unverified users.");
    }
}

