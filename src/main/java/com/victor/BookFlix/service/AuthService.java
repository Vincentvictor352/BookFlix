package com.victor.BookFlix.service;


import com.victor.BookFlix.dto.LoginRequest;
import com.victor.BookFlix.dto.LoginResponse;
import com.victor.BookFlix.dto.RegisterRequest;
import com.victor.BookFlix.entity.User;
import com.victor.BookFlix.jwt.JwtUtil;
import com.victor.BookFlix.repository.UserRepository;
import com.victor.BookFlix.role.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // 🔹 Register a new user
    public void register(RegisterRequest request) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Create new user
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER.name())  // convert enum to string
                .active(true)
                .build();

        userRepository.save(user);
    }

    // 🔹 Login user and return JWT
    public LoginResponse login(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());

        if (userOpt.isEmpty()) {
            throw new RuntimeException("Invalid username or password");
        }

        User user = userOpt.get();

        // Check password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        // Generate JWT token with username and role
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());

        return new LoginResponse(token, user.getUsername(), user.getRole());
    }
}
