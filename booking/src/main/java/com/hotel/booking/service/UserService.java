package com.hotel.booking.service;

import com.hotel.booking.exception.ResourceNotFoundException;
import com.hotel.booking.exception.UserNotFoundByEmailException;
import com.hotel.booking.model.User;
import com.hotel.booking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.hotel.booking.dto.UserResponse;
import com.hotel.booking.dto.CreateUserRequest;
import com.hotel.booking.dto.UpdateUserRequest;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse saveUser(CreateUserRequest request) {
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(request.password());
        user.setPhoneNumber(request.phoneNumber());

        User savedUser = userRepository.save(user);
        return mapToUserResponse(savedUser);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToUserResponse)
                .toList();
    }


    public UserResponse getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::mapToUserResponse)
                .orElseThrow(() -> new ResourceNotFoundException("User" , id));
    }

    //method for GUEST to update user profile
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User" , id));
        //Better design (not forced to modify this function)
        //Update the mapper function only if needed
        this.updateUserFields(existingUser, request);

        User updatedUser = userRepository.save(existingUser);
        return mapToUserResponse(updatedUser);
    }
    //method for ADMIN to change user status(No authority to change other fields)
    public UserResponse changeUserStatus(Long userID, boolean status){
        User existingUser = userRepository.findById(userID).orElseThrow(() -> new ResourceNotFoundException("User" , userID));
        existingUser.setStatus(status);
        User updatedUser = userRepository.save(existingUser);
        return mapToUserResponse(updatedUser);
    }
    public UserResponse getUserbyEmail(String email){
        return userRepository.findByEmail(email)
                .map(this::mapToUserResponse)
                .orElseThrow(() -> new UserNotFoundByEmailException(email));
    }
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // to Update one method only

    private void updateUserFields(User user, UpdateUserRequest request) {
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPhoneNumber(request.phoneNumber());
        user.setImageUrl(request.imageURL());
    }
    // MAPPER
    private UserResponse mapToUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getImageUrl(),
                user.getRole(),
                user.getStatus()
        );
    }

}

