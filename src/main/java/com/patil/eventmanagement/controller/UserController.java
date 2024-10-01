package com.patil.eventmanagement.controller;

import com.patil.eventmanagement.dto.response.UserResponse;
import com.patil.eventmanagement.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserResponse> findUserById(@PathVariable long id) {
        UserResponse user = userService.findUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserResponse> findUserByEmail(@PathVariable String username) {
        UserResponse user = userService.findUserByUsername(username);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }




}
