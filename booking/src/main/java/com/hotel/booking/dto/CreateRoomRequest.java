package com.hotel.booking.dto;

import com.hotel.booking.model.Enums.RoomType;

public record CreateRoomRequest(
        boolean availability,
        double price,
        int roomNumber,
        RoomType roomType,
        Long hotelID,
        String imageURL
) {
}
