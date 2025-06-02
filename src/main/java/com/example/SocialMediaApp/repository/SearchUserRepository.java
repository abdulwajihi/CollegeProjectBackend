package com.example.SocialMediaApp.repository;

import com.example.SocialMediaApp.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SearchUserRepository extends JpaRepository<User,Long> {
        // Get all users
        List<User> findAll();

        // Search by username (case-insensitive, partial match)
        List<User> findByUsernameContainingIgnoreCase(String username);

        @Query("select u from User u where u.firstName LIKE%:query% OR u.lastName LIKE%:query% OR u.email LIKE%:query% OR u.username LIKE%:query%")
        List<User> searchUser(@Param("query") String query);

        Optional<User> findByUsername(String username);

}

