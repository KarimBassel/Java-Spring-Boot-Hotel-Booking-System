package com.hotel.booking.dto;

public record UpdateUserRequest(
        String name,
        String email,
        String phoneNumber,
        String imageURL
) {}

