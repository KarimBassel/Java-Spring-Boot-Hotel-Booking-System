package com.hotel.booking.controller;

import com.hotel.booking.dto.CreateUserRequest;
import com.hotel.booking.dto.ProfileResponse;
import com.hotel.booking.dto.UpdateUserRequest;
import com.hotel.booking.dto.UserResponse;
import com.hotel.booking.service.CurrentUserService;
import com.hotel.booking.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private CurrentUserService currentUserService;

    @PermitAll
    @PostMapping
    public UserResponse createUser(@RequestBody CreateUserRequest user) {
        return userService.saveUser(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    // Will be used by Admin Dashboard
    // getUserProfile used by the user
    @PreAuthorize("@userSecurity.isOwner(#id) or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // No Need to verify the user id
    //User Id already retrieved from current authenticated user
    @GetMapping("/profile")
    public UserResponse getUserProfile() {
        return userService.getUserById(currentUserService.getCurrentUserId());
    }

    @PutMapping
    public UserResponse updateUser(@RequestBody UpdateUserRequest updatedUser) {
        // Current Authenticated User can only update their profile
        Long UserID = currentUserService.getCurrentUserId();
        return userService.updateUser(UserID, updatedUser);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}