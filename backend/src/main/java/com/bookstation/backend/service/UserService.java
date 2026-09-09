package com.bookstation.backend.service;

import com.bookstation.backend.dto.request.LoginRequest;
import com.bookstation.backend.dto.request.UserRequest;
import com.bookstation.backend.dto.response.AuthResponse;

public interface UserService {

    AuthResponse register(UserRequest request);
    AuthResponse login(LoginRequest request);

}
