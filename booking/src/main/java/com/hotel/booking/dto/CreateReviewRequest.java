package com.hotel.booking.dto;

public record CreateReviewRequest(
         Long hotelID,
         double rating,
         String comment
) {
}
