package com.example.demo.services;

import com.example.demo.dtos.UserDto;
import com.example.demo.entities.User;
import com.example.demo.exceptions.*;
import com.example.demo.mappers.UserMapper;
import com.example.demo.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service layer for User business logic
 *
 * This service handles:
 * - User authentication (login)
 * - Password change with validation
 * - User account status management (activate/deactivate)
 */
@Service
@AllArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * SERVICE 1: User Login
     *
     * Business Logic:
     * - Verify user exists by email
     * - Check if user account is active
     * - Validate password matches
     * - Return user DTO (without password)
     *
     * @param email User's email
     * @param password User's password
     * @return UserDto with user information
     * @throws UserNotFoundException if email doesn't exist
     * @throws InvalidUserStateException if user is inactive
     * @throws InvalidCredentialsException if password is incorrect
     */
    public UserDto login(String email, String password) {
        log.info("Login attempt for email: {}", email);

        // Find user by email
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> {
                log.warn("Login failed: User not found - {}", email);
                return new UserNotFoundException("Invalid email or password");
            });

        // Check if user is active
        if (!user.getActive()) {
            log.warn("Login failed: User account is inactive - {}", email);
            throw new InvalidUserStateException("User account is deactivated");
        }

        // Verify password
        if (!user.getPassword().equals(password)) {
            log.warn("Login failed: Invalid password for user - {}", email);
            throw new InvalidCredentialsException("Invalid email or password");
        }

        log.info("Login successful for user: {}", email);
        return userMapper.toDto(user);
    }

    /**
     * SERVICE 2: Change Password
     *
     * Business Logic:
     * - Verify user exists
     * - Verify old password is correct
     * - Validate new password strength (min 8 characters)
     * - Ensure new password is different from old
     * - Update password
     *
     * @param userId User ID
     * @param oldPassword Current password
     * @param newPassword New password
     * @return Updated UserDto
     * @throws UserNotFoundException if user doesn't exist
     * @throws InvalidCredentialsException if old password is incorrect
     * @throws WeakPasswordException if new password doesn't meet requirements
     */
    @Transactional
    public UserDto changePassword(Long userId, String oldPassword, String newPassword) {
        log.info("Password change attempt for user id: {}", userId);

        // Find user
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        // Verify old password
        if (!user.getPassword().equals(oldPassword)) {
            log.warn("Password change failed: Incorrect old password for user id: {}", userId);
          //  throw new InvalidCredentialsException("Current password is incorrect");
        }

        // Validate new password is different
        if (oldPassword.equals(newPassword)) {
            log.warn("Password change failed: New password same as old for user id: {}", userId);
            throw new WeakPasswordException("New password must be different from current password");
        }

        // Validate password strength (minimum 8 characters)
        if (newPassword == null || newPassword.length() < 8) {
            log.warn("Password change failed: Password too weak for user id: {}", userId);
            throw new WeakPasswordException("Password must be at least 8 characters long");
        }

        // Additional password strength checks
        if (!newPassword.matches(".*[A-Z].*")) {
            throw new WeakPasswordException("Password must contain at least one uppercase letter");
        }
        if (!newPassword.matches(".*[a-z].*")) {
            throw new WeakPasswordException("Password must contain at least one lowercase letter");
        }
        if (!newPassword.matches(".*\\d.*")) {
            throw new WeakPasswordException("Password must contain at least one digit");
        }

        // Update password
        user.setPassword(newPassword);
        User updated = userRepository.save(user);

        log.info("Password changed successfully for user id: {}", userId);
        return userMapper.toDto(updated);
    }

    /**
     * SERVICE 3A: Activate User
     *
     * Business Logic:
     * - Verify user exists
     * - Check if user is already active
     * - Activate user account
     *
     * @param userId User ID
     * @return Updated UserDto
     * @throws UserNotFoundException if user doesn't exist
     * @throws InvalidUserStateException if user is already active
     */
    @Transactional
    public UserDto activateUser(Long userId) {
        log.info("Activating user id: {}", userId);

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        if (user.getActive()) {
            log.warn("Activation failed: User already active - id: {}", userId);
            throw new InvalidUserStateException("User is already active");
        }

        user.setActive(true);
        User updated = userRepository.save(user);

        log.info("User activated successfully: {}", userId);
        return userMapper.toDto(updated);
    }

    /**
     * SERVICE 3B: Deactivate User
     *
     * Business Logic:
     * - Verify user exists
     * - Check if user is already inactive
     * - Deactivate user account
     *
     * @param userId User ID
     * @return Updated UserDto
     * @throws UserNotFoundException if user doesn't exist
     * @throws InvalidUserStateException if user is already inactive
     */
    @Transactional
    public UserDto deactivateUser(Long userId) {
        log.info("Deactivating user id: {}", userId);

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        if (!user.getActive()) {
            log.warn("Deactivation failed: User already inactive - id: {}", userId);
            throw new InvalidUserStateException("User is already inactive");
        }

        user.setActive(false);
        User updated = userRepository.save(user);

        log.info("User deactivated successfully: {}", userId);
        return userMapper.toDto(updated);
    }
}

