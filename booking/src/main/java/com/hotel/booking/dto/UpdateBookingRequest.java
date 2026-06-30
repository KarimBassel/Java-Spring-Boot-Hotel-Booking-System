package com.hotel.booking.dto;
import com.hotel.booking.model.Enums.Status;

public record UpdateBookingRequest(
        Status status
) {
}
