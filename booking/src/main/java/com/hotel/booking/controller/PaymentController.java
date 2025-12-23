package com.hotel.booking.controller;

import com.hotel.booking.dto.BookingResponse;
import com.hotel.booking.dto.PaymentConfirmationResponse;
import com.hotel.booking.dto.PaymentIntentResponse;
import com.hotel.booking.model.Booking;
import com.hotel.booking.model.Payment;
import com.hotel.booking.service.BookingService;
import com.hotel.booking.service.MockPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {


    @Autowired
    private MockPaymentService paymentService;

    //to get payment amount
    @Autowired
    private BookingService bookingService;

    @GetMapping("/create-intent/{booking_id}")
    public PaymentIntentResponse CreatePaymentIntent(@PathVariable Long bookingID){
        BookingResponse obj = bookingService.getBookingbyID(bookingID);
        return paymentService.createPaymentIntent(bookingID , obj.totalPayment());
    }

    @GetMapping("/confirm-payment")
    public PaymentConfirmationResponse confirmPayment(@PathVariable Long bookingID){
        BookingResponse obj = bookingService.getBookingbyID(bookingID);
        return paymentService.confirmPayment(bookingID , "MOCK_PI"+obj.bookingID());
    }



}
