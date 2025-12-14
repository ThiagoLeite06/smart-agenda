package com.thiagoleite06.smartagenda.authentication.service;

import com.thiagoleite06.smartagenda.authentication.dto.LoginRequest;
import com.thiagoleite06.smartagenda.authentication.dto.LoginResponse;
import com.thiagoleite06.smartagenda.authentication.dto.RegisterRequest;
import com.thiagoleite06.smartagenda.authentication.entity.User;
import com.thiagoleite06.smartagenda.authentication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public LoginResponse register(RegisterRequest request) {
        log.info("Registering new user with email: {}", request.getEmail());

        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        // Create new user
        User user = User.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .isActive(true)
                .build();

        user = userRepository.save(user);
        log.info("User registered successfully: {}", user.getEmail());

        // Generate JWT token
        String token = jwtService.generateToken(user);

        return new LoginResponse(
                token,
                user.getEmail(),
                user.getFullName(),
                jwtService.getExpiration()
        );
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        log.info("Login attempt for email: {}", request.getEmail());

        // Find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            log.warn("Invalid password for email: {}", request.getEmail());
            throw new BadCredentialsException("Invalid email or password");
        }

        // Check if user is active
        if (!user.getIsActive() || user.getDeletedAt() != null) {
            log.warn("Inactive user attempted to login: {}", request.getEmail());
            throw new BadCredentialsException("Account is inactive");
        }

        log.info("User logged in successfully: {}", user.getEmail());

        // Generate JWT token
        String token = jwtService.generateToken(user);

        return new LoginResponse(
                token,
                user.getEmail(),
                user.getFullName(),
                jwtService.getExpiration()
        );
    }
}
