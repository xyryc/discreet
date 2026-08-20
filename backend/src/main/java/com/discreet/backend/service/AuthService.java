package com.discreet.backend.service;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.discreet.backend.dto.AuthResponse;
import com.discreet.backend.dto.LoginRequest;
import com.discreet.backend.dto.RegisterRequest;
import com.discreet.backend.model.User;
import com.discreet.backend.repository.UserRepository;
import com.discreet.backend.security.JwtUtils;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    // inject UserRepository and password encoder
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
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
                passwordEncoder.encode(request.getPassword()),
                handle);
        userRepository.save(newUser);

        // return the AuthResponse DTO (Token + User data, no raw password)
        String token = jwtUtils.generateToken((newUser.getId()), newUser.getEmail());
        AuthResponse.UserDto userDto = new AuthResponse.UserDto(
                newUser.getId(),
                newUser.getDisplayName(),
                newUser.getEmail(),
                newUser.getHandle(),
                newUser.getBio());

        return new AuthResponse(token, userDto);
    }

    public AuthResponse login(LoginRequest request) {
        // look for user with same email in db
        // if not found throw an error
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password."));

        // check if password matches
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password.");
        }

        // credentials are valid, then generate session token and return user profile
        String token = jwtUtils.generateToken(user.getId(), user.getEmail());
        AuthResponse.UserDto userDto = new AuthResponse.UserDto(
                user.getId(),
                user.getDisplayName(),
                user.getEmail(),
                user.getHandle(), user.getBio());

        return new AuthResponse(token, userDto);
    }

}
