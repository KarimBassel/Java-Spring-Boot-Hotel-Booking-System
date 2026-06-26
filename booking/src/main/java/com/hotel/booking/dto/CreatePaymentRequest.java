package com.hotel.booking.dto;

public record CreatePaymentRequest(
        Long BookingID,
        double Amount
) {
}
