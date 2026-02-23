package com.hotel.booking.auth.dto;

public record AuthLoginResponse(
        String jwtToken,
        Long user_id
) {
}
