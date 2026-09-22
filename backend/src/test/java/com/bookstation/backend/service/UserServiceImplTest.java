package com.bookstation.backend.service;

import com.bookstation.backend.domain.User;
import com.bookstation.backend.dto.request.LoginRequest;
import com.bookstation.backend.dto.request.UserRequest;
import com.bookstation.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Key;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Key jwtKey;

    private User user;
    private UserRequest userRequest;
    private LoginRequest loginRequest;



}
