package com.bookstation.backend.service;

import com.bookstation.backend.domain.User;
import com.bookstation.backend.dto.request.LoginRequest;
import com.bookstation.backend.dto.request.UserRequest;
import com.bookstation.backend.dto.response.AuthResponse;
import com.bookstation.backend.repository.UserRepository;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private Key jwtKey;
    private UserServiceImpl userService;

    private User user;
    private UserRequest userRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {

        String secret = "test-secret-key-for-testing-must-be-32-bytes-long!!";
        jwtKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        userService = new UserServiceImpl(userRepository, passwordEncoder, jwtKey);

        user = User.builder()
                .id(1L)
                .username("usertest")
                .email("user@test.com")
                .password("encodedPassword")
                .build();

        userRequest = new UserRequest();
        userRequest.setUsername(user.getUsername());
        userRequest.setEmail(user.getEmail());
        userRequest.setPassword("passwordtest");

        loginRequest = new LoginRequest();
        loginRequest.setUsername(user.getUsername());
        loginRequest.setPassword("passwordtest");
    }

    @Test
    void register_shouldRegisterUserAndReturnAuthResponse() {
        //Arrange
        when(userRepository.existsByUsername("usertest")).thenReturn(false);
        when(passwordEncoder.encode("passwordtest")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        //Act
        AuthResponse result = userService.register(userRequest);
        //Assert
        assertThat(result.getToken()).isNotNull();
        assertThat(result.getUsername()).isEqualTo("usertest");
        assertThat(result.getEmail()).isEqualTo("user@test.com");

        verify(userRepository).existsByUsername("usertest");
        verify(passwordEncoder).encode("passwordtest");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_shouldThrowWhenUsernameAlreadyExists() {
        //Arrange
        when(userRepository.existsByUsername("usertest")).thenReturn(true);
        //Act
        //Assert
        assertThatThrownBy(() -> userService.register(userRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Username already exists");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void login_shouldAuthenticateAndReturnAuthResponse() {
        //Arrange
        when(userRepository.findByUsername("usertest")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("passwordtest", "encodedPassword")).thenReturn(true);
        //Act
        AuthResponse result = userService.login(loginRequest);
        //Assert
        assertThat(result.getToken()).isNotNull();
        assertThat(result.getUsername()).isEqualTo("usertest");
        assertThat(result.getMessage()).isEqualTo("Login successful");
    }

    @Test
    void login_shouldThrowWhenUserNotFound() {
        //Arrange
        when(userRepository.findByUsername("usertest")).thenReturn(Optional.empty());
        //Act
        //Assert
        assertThatThrownBy(() -> userService.login(loginRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found");
    }

    @Test
    void login_shouldThrowWhenPasswordDoesNotMatch() {
        //Arrange
        when(userRepository.findByUsername("usertest")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("passwordtest", "encodedPassword")).thenReturn(false);
        //Act
        //Assert
        assertThatThrownBy(() -> userService.login(loginRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Invalid password");
    }

}
