package com.hotel.booking.service;

import com.hotel.booking.model.User;
import com.hotel.booking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hotel.booking.dto.UserResponse;
import com.hotel.booking.dto.CreateUserRequest;
import com.hotel.booking.dto.UpdateUserRequest;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // CREATE
    public UserResponse saveUser(CreateUserRequest request) {
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(request.password());
        user.setPhoneNumber(request.phoneNumber());

        User savedUser = userRepository.save(user);
        return mapToUserResponse(savedUser);
    }

    // READ ALL
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToUserResponse)
                .toList();
    }


    public UserResponse getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::mapToUserResponse)
                .orElse(null);
    }

    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser == null) return null;

        existingUser.setName(request.name());
        existingUser.setEmail(request.email());
        existingUser.setPhoneNumber(request.phoneNumber());

        User updatedUser = userRepository.save(existingUser);
        return mapToUserResponse(updatedUser);
    }
    public UserResponse getUserbyEmail(String email){
        return userRepository.findByEmail(email)
                .map(this::mapToUserResponse)
                .orElse(null);
    }
    // DELETE
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // MAPPER
    private UserResponse mapToUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRole()
        );
    }
}

