package com.victor.BookFlix.service;

import com.victor.BookFlix.dto.UserDTO;
import com.victor.BookFlix.entity.User;
import com.victor.BookFlix.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service

public interface UserService {
    User save(User user);
    boolean authenticate(String email, String password);
    List<UserDTO> findAll();
    UserDTO findById(Long id);
    UserDTO update(Long id, User userDetails);
    void delete(Long id);
    UserDTO convertToDTO(User user);
}

