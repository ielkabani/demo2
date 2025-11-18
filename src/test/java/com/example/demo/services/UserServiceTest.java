package com.example.demo.services;

import com.example.demo.dtos.UserDto;
import com.example.demo.entities.User;
import com.example.demo.exceptions.*;
import com.example.demo.mappers.UserMapper;
import com.example.demo.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit Tests for UserService
 *
 * This test class demonstrates:
 * 1. Mocking dependencies (UserRepository, UserMapper)
 * 2. Testing business logic in isolation
 * 3. Verifying exception handling
 * 4. Using Arrange-Act-Assert pattern
 * 5. Organizing tests with @Nested classes
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Unit Tests")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private UserDto testUserDto;

    /**
     * This method runs before EACH test
     * It sets up fresh test data to ensure test isolation
     */
    @BeforeEach
    void setUp() {
        // Create a test user entity
        testUser = User.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .password("OldPass123")
                .active(true)
                .build();

        // Create a test user DTO (what the service returns)
        testUserDto = new UserDto(1L, "John Doe", "john@example.com", true);
    }

    /**
     * NESTED CLASS: Login Tests
     * Groups all login-related tests together
     */
    @Nested
    @DisplayName("Login Tests")
    class LoginTests {

        @Test
        @DisplayName("Should login successfully with valid credentials")
        void login_WithValidCredentials_ShouldReturnUserDto() {
            // ARRANGE: Set up the scenario
            // When repository.findByEmail() is called, return our test user
            when(userRepository.findByEmail("john@example.com"))
                    .thenReturn(Optional.of(testUser));
            // When mapper.toDto() is called, return our test DTO
            when(userMapper.toDto(testUser))
                    .thenReturn(testUserDto);

            // ACT: Execute the method being tested
            UserDto result = userService.login("john@example.com", "OldPass123");

            // ASSERT: Verify the results
            assertNotNull(result, "Result should not be null");
            assertEquals("john@example.com", result.getEmail());
            assertEquals("John Doe", result.getName());
            assertTrue(result.getActive());

            // Verify that the repository method was called exactly once
            verify(userRepository, times(1)).findByEmail("john@example.com");
            // Verify that the mapper was called exactly once
            verify(userMapper, times(1)).toDto(testUser);
        }

        @Test
        @DisplayName("Should throw UserNotFoundException when email doesn't exist")
        void login_WithNonExistentEmail_ShouldThrowUserNotFoundException() {
            // ARRANGE: Repository returns empty when user doesn't exist
            when(userRepository.findByEmail("nonexistent@example.com"))
                    .thenReturn(Optional.empty());

            // ACT & ASSERT: Verify exception is thrown
            UserNotFoundException exception = assertThrows(
                    UserNotFoundException.class,
                    () -> userService.login("nonexistent@example.com", "password")
            );

            // Verify exception message
            assertEquals("Invalid email or password", exception.getMessage());

            // Verify repository was called
            verify(userRepository, times(1)).findByEmail("nonexistent@example.com");
            // Verify mapper was NEVER called (since user wasn't found)
            verify(userMapper, never()).toDto(any());
        }

        @Test
        @DisplayName("Should throw InvalidUserStateException when user is inactive")
        void login_WithInactiveUser_ShouldThrowInvalidUserStateException() {
            // ARRANGE: Create inactive user
            testUser.setActive(false);
            when(userRepository.findByEmail("john@example.com"))
                    .thenReturn(Optional.of(testUser));

            // ACT & ASSERT
            InvalidUserStateException exception = assertThrows(
                    InvalidUserStateException.class,
                    () -> userService.login("john@example.com", "OldPass123")
            );

            assertEquals("User account is deactivated", exception.getMessage());
            verify(userRepository, times(1)).findByEmail("john@example.com");
            verify(userMapper, never()).toDto(any());
        }

        @Test
        @DisplayName("Should throw InvalidCredentialsException when password is incorrect")
        void login_WithWrongPassword_ShouldThrowInvalidCredentialsException() {
            // ARRANGE
            when(userRepository.findByEmail("john@example.com"))
                    .thenReturn(Optional.of(testUser));

            // ACT & ASSERT
            InvalidCredentialsException exception = assertThrows(
                    InvalidCredentialsException.class,
                    () -> userService.login("john@example.com", "WrongPassword")
            );

            assertEquals("Invalid email or password", exception.getMessage());
            verify(userRepository, times(1)).findByEmail("john@example.com");
            verify(userMapper, never()).toDto(any());
        }
    }

    /**
     * NESTED CLASS: Change Password Tests
     */
    @Nested
    @DisplayName("Change Password Tests")
    class ChangePasswordTests {

        @Test
        @DisplayName("Should change password successfully with valid inputs")
        void changePassword_WithValidInputs_ShouldUpdatePassword() {
            // ARRANGE
            String newPassword = "NewPass123";
            when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
            when(userRepository.save(any(User.class))).thenReturn(testUser);
            when(userMapper.toDto(testUser)).thenReturn(testUserDto);

            // ACT
            UserDto result = userService.changePassword(1L, "OldPass123", newPassword);

            // ASSERT
            assertNotNull(result);
            assertEquals(newPassword, testUser.getPassword(), "Password should be updated");
            verify(userRepository, times(1)).findById(1L);
            verify(userRepository, times(1)).save(testUser);
            verify(userMapper, times(1)).toDto(testUser);
        }

        @Test
        @DisplayName("Should throw UserNotFoundException when user doesn't exist")
        void changePassword_WithNonExistentUser_ShouldThrowUserNotFoundException() {
            // ARRANGE
            when(userRepository.findById(999L)).thenReturn(Optional.empty());

            // ACT & ASSERT
            UserNotFoundException exception = assertThrows(
                    UserNotFoundException.class,
                    () -> userService.changePassword(999L, "OldPass123", "NewPass123")
            );

            assertTrue(exception.getMessage().contains("User not found"));
            verify(userRepository, times(1)).findById(999L);
            verify(userRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw InvalidCredentialsException when old password is incorrect")
        void changePassword_WithWrongOldPassword_ShouldThrowInvalidCredentialsException() {
            // ARRANGE
            when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

            // ACT & ASSERT
            InvalidCredentialsException exception = assertThrows(
                    InvalidCredentialsException.class,
                    () -> userService.changePassword(1L, "WrongOldPassword", "NewPass123")
            );

            assertEquals("Current password is incorrect", exception.getMessage());
            verify(userRepository, times(1)).findById(1L);
            verify(userRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw WeakPasswordException when new password is same as old")
        void changePassword_WithSamePassword_ShouldThrowWeakPasswordException() {
            // ARRANGE
            when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

            // ACT & ASSERT
            WeakPasswordException exception = assertThrows(
                    WeakPasswordException.class,
                    () -> userService.changePassword(1L, "OldPass123", "OldPass123")
            );

            assertEquals("New password must be different from current password", exception.getMessage());
            verify(userRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw WeakPasswordException when password is too short")
        void changePassword_WithShortPassword_ShouldThrowWeakPasswordException() {
            // ARRANGE
            when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

            // ACT & ASSERT
            WeakPasswordException exception = assertThrows(
                    WeakPasswordException.class,
                    () -> userService.changePassword(1L, "OldPass123", "Short1")
            );

            assertEquals("Password must be at least 8 characters long", exception.getMessage());
            verify(userRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw WeakPasswordException when password lacks uppercase")
        void changePassword_WithNoUppercase_ShouldThrowWeakPasswordException() {
            // ARRANGE
            when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

            // ACT & ASSERT
            WeakPasswordException exception = assertThrows(
                    WeakPasswordException.class,
                    () -> userService.changePassword(1L, "OldPass123", "newpass123")
            );

            assertEquals("Password must contain at least one uppercase letter", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw WeakPasswordException when password lacks lowercase")
        void changePassword_WithNoLowercase_ShouldThrowWeakPasswordException() {
            // ARRANGE
            when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

            // ACT & ASSERT
            WeakPasswordException exception = assertThrows(
                    WeakPasswordException.class,
                    () -> userService.changePassword(1L, "OldPass123", "NEWPASS123")
            );

            assertEquals("Password must contain at least one lowercase letter", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw WeakPasswordException when password lacks digit")
        void changePassword_WithNoDigit_ShouldThrowWeakPasswordException() {
            // ARRANGE
            when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

            // ACT & ASSERT
            WeakPasswordException exception = assertThrows(
                    WeakPasswordException.class,
                    () -> userService.changePassword(1L, "OldPass123", "NewPassword")
            );

            assertEquals("Password must contain at least one digit", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw WeakPasswordException when password is null")
        void changePassword_WithNullPassword_ShouldThrowWeakPasswordException() {
            // ARRANGE
            when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

            // ACT & ASSERT
            WeakPasswordException exception = assertThrows(
                    WeakPasswordException.class,
                    () -> userService.changePassword(1L, "OldPass123", null)
            );

            assertEquals("Password must be at least 8 characters long", exception.getMessage());
        }
    }

    /**
     * NESTED CLASS: Activate User Tests
     */
    @Nested
    @DisplayName("Activate User Tests")
    class ActivateUserTests {

        @Test
        @DisplayName("Should activate inactive user successfully")
        void activateUser_WithInactiveUser_ShouldActivateUser() {
            // ARRANGE: Create an inactive user
            testUser.setActive(false);
            when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
            when(userRepository.save(any(User.class))).thenReturn(testUser);
            when(userMapper.toDto(testUser)).thenReturn(testUserDto);

            // ACT
            UserDto result = userService.activateUser(1L);

            // ASSERT
            assertNotNull(result);
            assertTrue(testUser.getActive(), "User should be activated");
            verify(userRepository, times(1)).findById(1L);
            verify(userRepository, times(1)).save(testUser);
            verify(userMapper, times(1)).toDto(testUser);
        }

        @Test
        @DisplayName("Should throw UserNotFoundException when user doesn't exist")
        void activateUser_WithNonExistentUser_ShouldThrowUserNotFoundException() {
            // ARRANGE
            when(userRepository.findById(999L)).thenReturn(Optional.empty());

            // ACT & ASSERT
            UserNotFoundException exception = assertThrows(
                    UserNotFoundException.class,
                    () -> userService.activateUser(999L)
            );

            assertTrue(exception.getMessage().contains("User not found"));
            verify(userRepository, times(1)).findById(999L);
            verify(userRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw InvalidUserStateException when user is already active")
        void activateUser_WithAlreadyActiveUser_ShouldThrowInvalidUserStateException() {
            // ARRANGE: User is already active
            testUser.setActive(true);
            when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

            // ACT & ASSERT
            InvalidUserStateException exception = assertThrows(
                    InvalidUserStateException.class,
                    () -> userService.activateUser(1L)
            );

            assertEquals("User is already active", exception.getMessage());
            verify(userRepository, times(1)).findById(1L);
            verify(userRepository, never()).save(any());
        }
    }

    /**
     * NESTED CLASS: Deactivate User Tests
     */
    @Nested
    @DisplayName("Deactivate User Tests")
    class DeactivateUserTests {

        @Test
        @DisplayName("Should deactivate active user successfully")
        void deactivateUser_WithActiveUser_ShouldDeactivateUser() {
            // ARRANGE: User is active
            testUser.setActive(true);
            when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
            when(userRepository.save(any(User.class))).thenReturn(testUser);
            when(userMapper.toDto(testUser)).thenReturn(testUserDto);

            // ACT
            UserDto result = userService.deactivateUser(1L);

            // ASSERT
            assertNotNull(result);
            assertFalse(testUser.getActive(), "User should be deactivated");
            verify(userRepository, times(1)).findById(1L);
            verify(userRepository, times(1)).save(testUser);
            verify(userMapper, times(1)).toDto(testUser);
        }

        @Test
        @DisplayName("Should throw UserNotFoundException when user doesn't exist")
        void deactivateUser_WithNonExistentUser_ShouldThrowUserNotFoundException() {
            // ARRANGE
            when(userRepository.findById(999L)).thenReturn(Optional.empty());

            // ACT & ASSERT
            UserNotFoundException exception = assertThrows(
                    UserNotFoundException.class,
                    () -> userService.deactivateUser(999L)
            );

            assertTrue(exception.getMessage().contains("User not found"));
            verify(userRepository, times(1)).findById(999L);
            verify(userRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw InvalidUserStateException when user is already inactive")
        void deactivateUser_WithAlreadyInactiveUser_ShouldThrowInvalidUserStateException() {
            // ARRANGE: User is already inactive
            testUser.setActive(false);
            when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

            // ACT & ASSERT
            InvalidUserStateException exception = assertThrows(
                    InvalidUserStateException.class,
                    () -> userService.deactivateUser(1L)
            );

            assertEquals("User is already inactive", exception.getMessage());
            verify(userRepository, times(1)).findById(1L);
            verify(userRepository, never()).save(any());
        }
    }
}

