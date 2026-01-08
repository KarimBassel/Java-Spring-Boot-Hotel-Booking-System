package com.hotel.booking.dto;

import com.hotel.booking.model.Room;

import java.util.List;

public record HotelResponse(
        Long id,
        String name,
        String location,
        String description,
        double averageRating,
        List<RoomResponse> Rooms
        )

{}
