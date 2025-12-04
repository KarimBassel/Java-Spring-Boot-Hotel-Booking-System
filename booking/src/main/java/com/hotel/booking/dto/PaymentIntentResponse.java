package com.hotel.booking.dto;
import com.hotel.booking.model.Enums.Status;
public record PaymentIntentResponse(
        String paymentId,
        String clientSecret,
        Status status
) {}
