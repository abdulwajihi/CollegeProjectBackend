package com.example.SocialMediaApp.repository;

import com.example.SocialMediaApp.dto.User;
import com.example.SocialMediaApp.dto.UserPreference;
import com.example.SocialMediaApp.dto.UserPreferenceProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface PreferenceRepository extends CrudRepository<UserPreference, Long> {

    @Query(value = "SELECT up.id AS userPrefId, up.user_id AS userId, pt.id AS preferenceTypeId, pt.name AS preferenceName " +
            "FROM user_preference up " +
            "JOIN preference_type pt ON up.preference_type_id = pt.id " +
            "WHERE up.user_id = :userId", nativeQuery = true)
    List<UserPreferenceProjection> getUserPreferencesWithNamesByUserId(Long userId);
    List<UserPreference> findByUser(User user);


//    @Query(value = "SELECT up.id AS userPrefId, up.user_id AS userId, pt.preference_type_id AS preferenceTypeId, pt.name AS preferenceName " +
//            "FROM user_preference up " +
//            "JOIN preference_type pt ON up.preference_type_id = pt.preference_type_id " +
//            "WHERE up.user_id = :userId", nativeQuery = true)
//    List<UserPreferenceProjection> getUserPreferencesWithNamesByUserId(Long userId);
}


