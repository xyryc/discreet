package com.discreet.backend.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.discreet.backend.dto.AuthResponse;
import com.discreet.backend.dto.RegisterRequest;
import com.discreet.backend.model.User;
import com.discreet.backend.repository.UserRepository;

@Service
public class AuthService {
    private final UserRepository userRepository;

    // inject UserRepository
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AuthResponse register(RegisterRequest request) {
        // check if email already taken using our repository
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already registered: " + request.getEmail());
        }

        // generate unique user ID and a discreet handle(eg. @discreet/alex_8f2a)
        String userId = "usr_" + UUID.randomUUID().toString().substring(0, 8);
        String randomHex = UUID.randomUUID().toString().substring(0, 4);
        String cleanName = request.getDisplayName().toLowerCase().replaceAll("\\s+", "");
        String handle = "@discreet/" + cleanName + "_" + randomHex;

        // create the user entity and save to database
        User newUser = new User(userId,
                request.getDisplayName(),
                request.getEmail(),
                request.getPassword(),
                handle);
        userRepository.save(newUser);

        // return the AuthResponse DTO (Token + User data, no raw password)
        String mockJwtToken = "discreet_token_" + UUID.randomUUID().toString();
        AuthResponse.UserDto userDto = new AuthResponse.UserDto(
                newUser.getId(),
                newUser.getDisplayName(),
                newUser.getEmail(),
                newUser.getHandle(),
                newUser.getBio());

        return new AuthResponse(mockJwtToken, userDto);
    }
}
