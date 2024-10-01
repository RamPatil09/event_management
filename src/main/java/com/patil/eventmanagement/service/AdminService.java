package com.patil.eventmanagement.service;

import com.patil.eventmanagement.dto.response.UserResponse;
import com.patil.eventmanagement.entity.Users;

import java.util.List;

public interface AdminService {
    List<UserResponse> findAllUsers();
    boolean deleteUserById(long id);
}
