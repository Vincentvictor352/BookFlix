package com.victor.BookFlix.service;

import com.victor.BookFlix.dto.UserDTO;
import com.victor.BookFlix.entity.User;

public interface AuthService {
    UserDTO register(User user);
    String login(String email, String password);
}
