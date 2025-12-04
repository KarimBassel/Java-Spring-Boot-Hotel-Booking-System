package com.hotel.booking.service;
import com.hotel.booking.dto.PaymentIntentResponse;
import com.hotel.booking.dto.PaymentConfirmationResponse;

public interface PaymentService {

    public PaymentIntentResponse createPaymentIntent(Long bookingId, double amount);

    public PaymentConfirmationResponse confirmPayment(Long BookingID, String paymentId);
}
