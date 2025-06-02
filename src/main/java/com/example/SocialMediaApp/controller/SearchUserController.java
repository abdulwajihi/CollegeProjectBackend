package com.example.SocialMediaApp.controller;

import com.example.SocialMediaApp.dto.User;
import com.example.SocialMediaApp.dto.UserDTO;
import com.example.SocialMediaApp.service.SearchUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class SearchUserController {
    @Autowired
    SearchUserService searchUserService;

    @GetMapping
    public List<UserDTO> getAllUser(){
      return searchUserService.getAllUsers();
    }
    // Search by username
    @GetMapping("/searchByUsername")
    public List<UserDTO> searchUsers(@RequestParam("username") String username) {
        return searchUserService.searchUsersByUsername(username);
    }

//    @GetMapping("/searchUser")
//    public List<User> userSearch(@RequestParam("query") String query){
//        List<User> users = searchUserService.searchUser(query);
//        return users;
//    }
}
