package com.hotel.booking.dto;

public record UpdateHotelRequest(
        String name,
        String location,
        String description,
        String image_url
) {
}
