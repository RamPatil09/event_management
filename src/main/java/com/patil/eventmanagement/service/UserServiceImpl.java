package com.patil.eventmanagement.service;

import com.patil.eventmanagement.dto.request.RegisterRequest;
import com.patil.eventmanagement.dto.response.UserResponse;
import com.patil.eventmanagement.entity.ERoles;
import com.patil.eventmanagement.entity.Users;
import com.patil.eventmanagement.exception.EventManagementCustomException;
import com.patil.eventmanagement.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Long saveUser(RegisterRequest registerRequest) {
        log.info("Inside save user method!");

        // Check if the user already exists by email
        Optional<Users> userByEmail = userRepository.findByEmail(registerRequest.getEmail());
        if (userByEmail.isPresent()) {
            log.info("User already exists with email: {}", registerRequest.getEmail());
            throw new EventManagementCustomException(
                    "User already exists with email: " + registerRequest.getEmail(), // Custom exception for existing user
                    "USER_ALREADY_EXISTS"
            );
        }

        // Create a new user entity from the registration request
        Users user = Users.builder()
                .firstname(registerRequest.getFirstname())
                .lastname(registerRequest.getLastname())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword())) // Encode the password
                .contactNumber(registerRequest.getContactNumber())
                .role(ERoles.ROLE_USER)
                .build();

        Users savedUser = userRepository.save(user);
        log.info("User saved successfully with ID: {}", savedUser.getId());

        return savedUser.getId();
    }

    @Override
    public UserResponse findUserById(long id) {
        log.info("Inside find user by Id method.");
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new EventManagementCustomException("User not found!", "USER_NOT_FOUND"));

        if (user != null) {
            log.info("User found with ID:{} ", id);
            UserResponse userResponse = UserResponse
                    .builder()
                    .id(user.getId())
                    .firstname(user.getFirstname())
                    .lastname(user.getLastname())
                    .email(user.getEmail())
                    .contactNumber(user.getContactNumber())
                    .build();
            return userResponse;
        }

        log.info("User not found with given id:{}", id);
        return null;
    }

    @Override
    public UserResponse findUserByUsername(String username) {
        log.info("Inside find user by Id method.");
        Users user = userRepository.findByEmail(username)
                .orElseThrow(() -> new EventManagementCustomException("User not found with username: " + username, "USER_NOT_FOUND"));

        if (user != null) {
            log.info("User found with username:{} ", username);
            UserResponse userResponse = UserResponse
                    .builder()
                    .id(user.getId())
                    .firstname(user.getFirstname())
                    .lastname(user.getLastname())
                    .email(user.getEmail())
                    .contactNumber(user.getContactNumber())
                    .build();
            return userResponse;
        }

        log.info("User not found with given username:{}", username);
        return null;
    }
}
