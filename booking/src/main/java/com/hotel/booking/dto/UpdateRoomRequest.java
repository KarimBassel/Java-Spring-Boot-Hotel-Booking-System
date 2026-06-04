package com.hotel.booking.dto;

import com.hotel.booking.model.Enums.RoomType;

public record UpdateRoomRequest(
        Long id,
        boolean availability,
        double price,
        int roomNumber,
        RoomType roomType,
        Long hotelID,
        String imageURL
) {
}
