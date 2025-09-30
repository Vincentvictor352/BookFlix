package com.victor.BookFlix.dto;

import lombok.Data;

@Data
public class LoginRequest {

    private String username; // or email
    private String password;
}
