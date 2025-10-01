package com.victor.BookFlix.controller;

import com.victor.BookFlix.dto.UserDTO;
import com.victor.BookFlix.entity.User;
import com.victor.BookFlix.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody User user) {
        UserDTO createdUser = authService.register(user);
        return ResponseEntity.ok(createdUser);
    }

    //  Login and return token
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email,
                                        @RequestParam String password) {
        String token = authService.login(email, password);
        return ResponseEntity.ok(token);
    }
}
