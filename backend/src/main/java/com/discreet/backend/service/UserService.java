package com.discreet.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.discreet.backend.dto.AuthResponse;
import com.discreet.backend.dto.UpdateProfileRequest;
import com.discreet.backend.model.User;
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
                user.getBio(),
                user.getStatus(),
                user.getImageProfile()))
                .toList();
    }

    // fetch a single user by their unique ID
    public AuthResponse.UserDto getUserById(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        return new AuthResponse.UserDto(
                user.getId(),
                user.getDisplayName(),
                user.getEmail(),
                user.getHandle(),
                user.getBio(),
                user.getStatus(),
                user.getImageProfile());
    }

    // update user profile
    public AuthResponse.UserDto updateProfile(String userId, UpdateProfileRequest request) {
        // fetch users from database
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        // update fields only if provided
        if (request.getDisplayName() != null && !request.getDisplayName().trim().isEmpty()) {
            user.setDisplayName(request.getDisplayName().trim());
        }

        if (request.getBio() != null) {
            user.setBio(request.getBio().trim());
        }

        if (request.getStatus() != null && !request.getStatus().trim().isEmpty()) {
            user.setStatus(request.getStatus().trim());
        }

        if (request.getImageProfile() != null) {
            user.setImageProfile(request.getImageProfile());
        }

        // save updated user to PostgreSQL/H2
        userRepository.save(user);

        return new AuthResponse.UserDto(
                user.getId(),
                user.getDisplayName(),
                user.getEmail(),
                user.getHandle(),
                user.getBio(),
                user.getStatus(),
                user.getImageProfile());
    }

    // update user profile avatar URL
    public AuthResponse.UserDto updateAvatar(String userId, String avatarUrl) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("User not found: " + userId));

        user.setImageProfile(avatarUrl);
        userRepository.save(user);

        return new AuthResponse.UserDto(
                user.getId(),
                user.getDisplayName(),
                user.getEmail(),
                user.getHandle(),
                user.getBio(), user.getStatus(),
                user.getImageProfile());
    }
}
