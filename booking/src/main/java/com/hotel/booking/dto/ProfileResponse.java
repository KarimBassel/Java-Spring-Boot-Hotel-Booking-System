package com.hotel.booking.dto;

import com.hotel.booking.model.Enums.Role;

public record ProfileResponse(
        long id,
        String name,
        String email,
        String phoneNumber,
        Role role,
        String ImageURL
) {
}
