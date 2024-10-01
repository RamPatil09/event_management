package com.patil.eventmanagement.service;

import com.patil.eventmanagement.dto.response.UserResponse;
import com.patil.eventmanagement.entity.Users;
import com.patil.eventmanagement.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final UserService userService;

    public AdminServiceImpl(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public List<UserResponse> findAllUsers() {
        log.info("Inside find all users method.");

        List<Users> allUsers = userRepository.findAll();

        List<UserResponse> userResponses = new ArrayList<>();

        allUsers.stream().forEach(users -> {
            UserResponse userResponse =
                    UserResponse.builder()
                            .id(users.getId())
                            .firstname(users.getFirstname())
                            .lastname(users.getLastname())
                            .email(users.getEmail())
                            .contactNumber(users.getContactNumber())
                            .build();
            userResponses.add(userResponse);
        });
        return userResponses;
    }

    @Override
    public boolean deleteUserById(long id) {
        log.info("Inside Delete user by id: {}", id);
        boolean isDeleted = false;
        log.info("Checking user is present or not.");
        UserResponse user = userService.findUserById(id);
        if (user != null) {
            userRepository.deleteById(id);
            log.info("User with id: {} deleted successfully.", id);
            isDeleted = true;
        }
        return isDeleted;
    }
}
