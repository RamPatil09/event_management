package com.patil.eventmanagement.controller;

import com.patil.eventmanagement.dto.response.UserResponse;
import com.patil.eventmanagement.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/home")
    public String userProfile() {
        return "Welcome to User profile";
    }


    @GetMapping("/id/{id}")
    public ResponseEntity<UserResponse> findUserById(@PathVariable long id) {
        UserResponse user = userService.findUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserResponse> findUserByEmail(@PathVariable String username) {
        UserResponse user = userService.findUserByUsername(username);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
