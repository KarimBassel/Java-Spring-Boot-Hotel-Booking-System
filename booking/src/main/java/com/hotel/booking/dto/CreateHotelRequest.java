package com.hotel.booking.dto;

public record CreateHotelRequest(
        String name,
        String location,
        String description,
        String image_url
) {
}
