package com.hotel.booking.dto;

import com.hotel.booking.model.Enums.Status;

import java.time.LocalDate;

public record UpdateBookingRequest(
        Status status
) {
}
