package com.example.SocialMediaApp.service;

import com.example.SocialMediaApp.dto.User;
import com.example.SocialMediaApp.dto.UserDTO;
import com.example.SocialMediaApp.repository.SearchUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchUserService {
    @Autowired
    SearchUserRepository searchUserRepository;
    public List<UserDTO> getAllUsers() {
        List<User> users = searchUserRepository.findAll();
        return users.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<UserDTO> searchUsersByUsername(String username) {
        List<User> users = searchUserRepository.findByUsernameContainingIgnoreCase(username);
        return users.stream().map(this::convertToDTO).collect(Collectors.toList());

    }

    public List<User> searchUser(String query){
        return searchUserRepository.searchUser(query);
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getGender(),
                user.getPhoneNumber()
        );
    }

}
