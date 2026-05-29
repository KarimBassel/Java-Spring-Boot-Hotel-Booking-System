package com.hotel.booking.dto;

import com.hotel.booking.model.Enums.Status;

import java.time.LocalDate;

public record BookingResponse(
        Long bookingID,
        Long HotelID,
        String hotelName,
        int roomNumber,
        LocalDate CheckIn,
        LocalDate CheckOut,
        double totalPayment,
        Status status
) {
}
