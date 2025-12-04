package com.hotel.booking.dto;
import com.hotel.booking.model.Enums.Status;
public record PaymentConfirmationResponse(
        String Payment_ID,
        Status status
)
{}
