package com.hotel.booking.security;

import com.hotel.booking.dto.UserResponse;
import com.hotel.booking.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component("userSecurity")
public class UserSecurity {

    private final UserRepository userRepository;

    public UserSecurity(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Checks if the authenticated user matches the given userId
     */
    public boolean isOwner(Long userId) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        Object principal =  authentication.getPrincipal();
        if(!(principal instanceof UserResponse)){
            return false;
        }
        UserResponse userResponse = (UserResponse) principal;

        return userResponse.id() == userId;
    }

}
