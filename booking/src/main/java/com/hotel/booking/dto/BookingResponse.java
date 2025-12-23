package com.hotel.booking.dto;

import com.hotel.booking.model.Enums.Status;

import java.util.Date;

public record BookingResponse(
        Long bookingID,
        String hotelName,
        int rommNumber,
        Date CheckIn,
        Date CheckOut,
        double totalPayment,
        Status status
) {
}
