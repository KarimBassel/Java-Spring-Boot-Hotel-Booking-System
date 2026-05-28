package com.hotel.booking.dto;

import com.hotel.booking.model.Enums.RoomType;

public record RoomResponse(
        Long id,
        int roomNumber,
        RoomType roomtype,
        double price,
        boolean availability,
        String image_url
)
{}
