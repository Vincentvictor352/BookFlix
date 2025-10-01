package com.victor.BookFlix.service.impl;

import com.victor.BookFlix.dto.UserDTO;
import com.victor.BookFlix.entity.User;
import com.victor.BookFlix.repository.UserRepository;
import com.victor.BookFlix.service.AuthService;
import com.victor.BookFlix.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {


    private final UserRepository userRepository;
    private final UserService userService;

    public AuthServiceImpl(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public UserDTO register(User user) {
        // check if user already exists
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("User already exists with email: " + user.getEmail());
        }
        User savedUser = userRepository.save(user);
        return userService.convertToDTO(savedUser);
    }

    @Override
    public String login(String email, String password) {
        User user = userRepository.findByEmail(email);

        if (user == null || !user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid email or password");
        }

        // ✅ For now, return a dummy token (replace later with JWT if needed)
        return "TOKEN_" + user.getId() + "_" + System.currentTimeMillis();
    }
    }
