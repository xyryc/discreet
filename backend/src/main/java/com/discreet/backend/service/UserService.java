package com.discreet.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.discreet.backend.dto.AuthResponse;
import com.discreet.backend.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    // inject UserRepository
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // business logic to get all users formatted as safe DTOs
    public List<AuthResponse.UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(user -> new AuthResponse.UserDto(
                user.getId(),
                user.getDisplayName(),
                user.getEmail(),
                user.getHandle(),
                user.getBio()))
                .toList();
    }
}
