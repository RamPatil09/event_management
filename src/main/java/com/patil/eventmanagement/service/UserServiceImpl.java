package com.patil.eventmanagement.service;

import com.patil.eventmanagement.dto.request.RegisterRequest;
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
}
