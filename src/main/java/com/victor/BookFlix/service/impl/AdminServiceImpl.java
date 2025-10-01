package com.victor.BookFlix.service.impl;

import com.victor.BookFlix.dto.AdminDTO;
import com.victor.BookFlix.entity.User;
import com.victor.BookFlix.repository.UserRepository;
import com.victor.BookFlix.service.AdminService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    public AdminServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<AdminDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> new AdminDTO(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getRole(),
                        user.isActive()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public String changeUserRole(Long userId, String newRole) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setRole(newRole);
        userRepository.save(user);
        return "User role updated to " + newRole;
    }

    @Override
    public String deactivateUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setActive(false);
        userRepository.save(user);
        return "User deactivated successfully";
    }

    @Override
    public String activateUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setActive(true);
        userRepository.save(user);
        return "User activated successfully";
    }
    }

