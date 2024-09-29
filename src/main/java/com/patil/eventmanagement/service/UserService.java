package com.patil.eventmanagement.service;

import com.patil.eventmanagement.dto.request.RegisterRequest;

public interface UserService {
    Long saveUser(RegisterRequest registerRequest);
}
