package com.hotel.booking.dto;
import java.time.LocalDate;

public record BookingRequest(
        long roomId,
        LocalDate checkInDate,
        LocalDate checkOutDate,
        int numberOfNights,
        double totalPrice
) {
}