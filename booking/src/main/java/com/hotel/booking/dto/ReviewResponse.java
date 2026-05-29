package com.hotel.booking.dto;

import java.time.LocalDate;

public record ReviewResponse(
         Long id,
         Long userId,
         String userName,
         Long hotelId,
         String hotelName,
         double review,
         String comment,
         LocalDate createdAt
) {
}
