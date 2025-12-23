package com.hotel.booking.dto;

import com.hotel.booking.model.Enums.Role;

public record UserResponse(
        long id,
        String name,
        String email,
        String phoneNumber,
        Role role
) {
}
