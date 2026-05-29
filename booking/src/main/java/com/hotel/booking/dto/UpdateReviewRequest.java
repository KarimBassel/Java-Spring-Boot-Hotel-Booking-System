package com.hotel.booking.dto;

public record UpdateReviewRequest(
        Long reviewID,
        double rating,
        String comment
) {
}
