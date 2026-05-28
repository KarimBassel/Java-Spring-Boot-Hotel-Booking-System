package com.hotel.booking.service;
import com.hotel.booking.dto.UserResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
// This Service acts as a central point of retrieval
@Service
public class CurrentUserService {

    public UserResponse getCurrentUser() {

        Authentication auth =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        return (UserResponse) auth.getPrincipal();
    }

    public Long getCurrentUserId() {
        return getCurrentUser().id();
    }

    public String getCurrentUserEmail() {
        return getCurrentUser().email();
    }
}
