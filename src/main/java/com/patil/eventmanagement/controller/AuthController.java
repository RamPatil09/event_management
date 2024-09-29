package com.patil.eventmanagement.controller;

import com.patil.eventmanagement.dto.request.LoginRequest;
import com.patil.eventmanagement.dto.request.RegisterRequest;
import com.patil.eventmanagement.security.service.JwtService;
import com.patil.eventmanagement.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    // Constructor to inject dependencies
    public AuthController(JwtService jwtService, AuthenticationManager authenticationManager, UserService userService) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    // Home page endpoint
    @GetMapping("/home")
    public String home() {
        logger.info("Home page accessed");
        return "Welcome to home page";
    }

    // Register a new user
    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        logger.info("Registering user: {}", registerRequest.getEmail());
        Long id = userService.saveUser(registerRequest);
        return new ResponseEntity<>("User registered with Id: " + id, HttpStatus.CREATED);
    }

    // Login and return JWT token
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest loginRequest) {
        logger.info("User attempting to log in: {}", loginRequest.getUsername());

        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        if (authentication.isAuthenticated()) {
            String token = jwtService.generateToken(loginRequest.getUsername());
            logger.info("Login successful, JWT generated for user: {}", loginRequest.getUsername());
            return new ResponseEntity<>(token, HttpStatus.OK);
        } else {
            logger.warn("Login failed for user: {}", loginRequest.getUsername());
            throw new UsernameNotFoundException("Invalid login credentials!");
        }
    }

    // Logout the user, invalidate session and token
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        logger.info("User attempting to log out");

        // Extract token from Authorization header
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            jwtService.invalidateToken(token);  // Invalidate the token if needed
            logger.info("Token invalidated");
        }

        // Clear security context and session
        SecurityContextHolder.clearContext();
        request.getSession().invalidate();

        return new ResponseEntity<>("Successfully logged out", HttpStatus.OK);
    }
}