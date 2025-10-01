package com.victor.BookFlix.service;

import com.victor.BookFlix.dto.AdminDTO;

import java.util.List;

public interface AdminService {
    List<AdminDTO> getAllUsers();
    String changeUserRole(Long userId, String newRole);
    String deactivateUser(Long userId);
    String activateUser(Long userId);
}
