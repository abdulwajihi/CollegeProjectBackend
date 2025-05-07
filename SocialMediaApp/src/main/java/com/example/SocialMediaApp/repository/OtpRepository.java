package com.example.SocialMediaApp.repository;

import com.example.SocialMediaApp.dto.Otp;
import com.example.SocialMediaApp.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface OtpRepository extends JpaRepository<Otp,Long> {
    List<Otp> findByUserAndPurpose(User user, String purpose);
}
