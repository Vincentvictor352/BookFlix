package com.victor.BookFlix.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminDTO {

    private Long id;
    private String username;
    private String email;
    private String role;
    private boolean active;
}
