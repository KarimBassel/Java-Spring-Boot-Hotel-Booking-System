package com.hotel.booking.service;

import com.hotel.booking.dto.CreateUserRequest;
import com.hotel.booking.dto.UpdateUserRequest;
import com.hotel.booking.dto.UserResponse;
import com.hotel.booking.model.Enums.Role;
import com.hotel.booking.model.User;
import com.hotel.booking.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Profile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Profile("test")
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setup() {
        user = new User();
        user.setId(1L);
        user.setName("Karim");
        user.setEmail("karim@example.com");
        user.setPhoneNumber("0123456789");
        user.setRole(Role.GUEST);
    }


    @Test
    void saveUser_savesAndReturnsUserResponse() {
        CreateUserRequest request = new CreateUserRequest(
                "Karim",
                "karim@example.com",
                "password",
                "0123456789"
        );

        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse response = userService.saveUser(request);

        assertNotNull(response);
        assertEquals("Karim", response.name());
        assertEquals("karim@example.com", response.email());
        verify(userRepository).save(any(User.class));
    }



    @Test
    void getAllUsers_returnsUserResponses() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserResponse> users = userService.getAllUsers();

        assertEquals(1, users.size());
        assertEquals("Karim", users.get(0).name());
    }


    @Test
    void getUserById_whenExists_returnsUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponse response = userService.getUserById(1L);

        assertNotNull(response);
        assertEquals("Karim", response.name());
    }

    @Test
    void getUserById_whenNotExists_returnsNull() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        UserResponse response = userService.getUserById(1L);

        assertNull(response);
    }


    @Test
    void updateUser_whenExists_updatesAndReturnsUser() {
        UpdateUserRequest request = new UpdateUserRequest(
                "Updated Name",
                "updated@example.com",
                "0999999999"
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse response = userService.updateUser(1L, request);

        assertNotNull(response);
        assertEquals("Updated Name", response.name());
        assertEquals("updated@example.com", response.email());
    }

    @Test
    void updateUser_whenNotExists_returnsNull() {
        UpdateUserRequest request = new UpdateUserRequest(
                "Name",
                "email",
                "phone"
        );

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        UserResponse response = userService.updateUser(1L, request);

        assertNull(response);
        verify(userRepository, never()).save(any());
    }


    @Test
    void getUserByEmail_whenExists_returnsUser() {
        when(userRepository.findByEmail("karim@example.com"))
                .thenReturn(Optional.of(user));

        UserResponse response = userService.getUserbyEmail("karim@example.com");

        assertNotNull(response);
        assertEquals("Karim", response.name());
    }

    @Test
    void getUserByEmail_whenNotExists_returnsNull() {
        when(userRepository.findByEmail("karim@example.com"))
                .thenReturn(Optional.empty());

        UserResponse response = userService.getUserbyEmail("karim@example.com");

        assertNull(response);
    }

    // ================= DELETE =================

    @Test
    void deleteUser_deletesById() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }
}
