package com.example.SocialMediaApp.repository;

import com.example.SocialMediaApp.dto.PreferenceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PreferenceTypeRepository extends JpaRepository<PreferenceType, Long> {
    Optional<PreferenceType> findByName(String name);
}
