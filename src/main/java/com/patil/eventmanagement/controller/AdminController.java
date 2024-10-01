package com.patil.eventmanagement.controller;

import com.patil.eventmanagement.dto.response.UserResponse;
import com.patil.eventmanagement.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/home")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String adminProfile() {
        return "Welcome to Admin profile";
    }

    @GetMapping("/allUsers")
    public ResponseEntity<List<UserResponse>> findAllUsers() {
        List<UserResponse> allUsers = adminService.findAllUsers();
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> deleteUserById(@PathVariable long id) {
        if (adminService.deleteUserById(id)) {
            return new ResponseEntity<>("User deleted with id: " + id, HttpStatus.OK);
        }
        return new ResponseEntity<>("Something went wrong", HttpStatus.NOT_FOUND);
    }
}
