package com.hotel.booking.dto;
import com.hotel.booking.model.Enums.Status;

import java.util.Date;
import java.time.LocalDate;

public record BookingRequest(
        long roomId,
        LocalDate checkInDate,
        LocalDate checkOutDate,
        int numberOfNights,
        double totalPrice
) {
}