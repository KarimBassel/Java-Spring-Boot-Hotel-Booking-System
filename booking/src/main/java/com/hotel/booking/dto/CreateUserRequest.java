package com.hotel.booking.dto;

public record CreateUserRequest(
        String name,
        String email,
        String password,
        String phoneNumber
) {}
