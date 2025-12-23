package com.hotel.booking.dto;

import com.hotel.booking.model.Enums.RoomType;

public record RoomResponse(
        Long id,
        int roomNumber,
        RoomType rootype,
        double price,
        boolean availiability
)
{}
