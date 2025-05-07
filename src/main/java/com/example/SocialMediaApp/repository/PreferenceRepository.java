package com.example.SocialMediaApp.repository;

import com.example.SocialMediaApp.dto.UserPreference;
import com.example.SocialMediaApp.dto.UserPreferenceProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PreferenceRepository extends CrudRepository<UserPreference, Long> {

    @Query(value = "SELECT up.id AS userPrefId, up.user_id AS userId, pt.id AS preferenceTypeId, pt.name AS preferenceName " +
            "FROM user_preference up JOIN preference_type pt ON up.preference_type_id = pt.id", nativeQuery = true)
    List<UserPreferenceProjection> getUserPreferencesWithNames();
}

