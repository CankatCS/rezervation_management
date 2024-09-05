package com.phoenix_dev.rezervation_management.service;

/**
 * @author Cankat Sezer
 */

import com.phoenix_dev.rezervation_management.dto.UserRegistrationDto;
import com.phoenix_dev.rezervation_management.exception.UserNotFoundException;
import com.phoenix_dev.rezervation_management.model.User;
import com.phoenix_dev.rezervation_management.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerNewUserThrowsExceptionWhenUsernameExists() {
        UserRegistrationDto userDto = new UserRegistrationDto();
        userDto.setUsername("existingUser");
        when(userRepository.existsByUsername("existingUser")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> userService.registerNewUser(userDto));
    }

    @Test
    void registerNewUserThrowsExceptionWhenEmailExists() {
        UserRegistrationDto userDto = new UserRegistrationDto();
        userDto.setEmail("existingEmail@example.com");
        when(userRepository.existsByEmail("existingEmail@example.com")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> userService.registerNewUser(userDto));
    }

    @Test
    void registerNewUserSavesAndReturnsUser() {
        UserRegistrationDto userDto = new UserRegistrationDto();
        userDto.setUsername("newUser");
        userDto.setPassword("password");
        userDto.setEmail("newUser@example.com");

        User user = new User();
        user.setUsername("newUser");
        user.setPassword("encodedPassword");
        user.setEmail("newUser@example.com");

        when(userRepository.existsByUsername("newUser")).thenReturn(false);
        when(userRepository.existsByEmail("newUser@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        assertEquals(user, userService.registerNewUser(userDto));
    }

    @Test
    void getUserByIdReturnsUser() {
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        assertEquals(user, userService.getUserById(1L));
    }

    @Test
    void getUserByIdThrowsExceptionWhenNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    void updateUserUpdatesAndReturnsUser() {
        UserRegistrationDto userDto = new UserRegistrationDto();
        userDto.setUsername("updatedUser");
        userDto.setPassword("newPassword");
        userDto.setEmail("updatedUser@example.com");

        User existingUser = new User();
        existingUser.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        User updatedUser = userService.updateUser(1L, userDto);
        assertEquals("updatedUser", updatedUser.getUsername());
        assertEquals("encodedNewPassword", updatedUser.getPassword());
        assertEquals("updatedUser@example.com", updatedUser.getEmail());
    }

    @Test
    void updateUserThrowsExceptionWhenNotFound() {
        UserRegistrationDto userDto = new UserRegistrationDto();
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(1L, userDto));
    }

    @Test
    void deleteUserByIdDeletesUser() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);
        userService.deleteUserById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteUserByIdThrowsExceptionWhenNotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> userService.deleteUserById(1L));
    }
}
