package com.patil.eventmanagement.service;

import com.patil.eventmanagement.dto.request.RegisterRequest;
import com.patil.eventmanagement.dto.response.UserResponse;
import com.patil.eventmanagement.entity.Users;

public interface UserService {
    Long saveUser(RegisterRequest registerRequest);
    UserResponse findUserById(long id);
    UserResponse findUserByUsername(String username);
}
