package com.hotel.booking.service;

import com.hotel.booking.dto.PaymentConfirmationResponse;
import com.hotel.booking.dto.PaymentIntentResponse;
import com.hotel.booking.model.Booking;
import com.hotel.booking.model.Enums.Status;
import com.hotel.booking.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Profile;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Profile("test")
@ExtendWith(MockitoExtension.class)
class MockPaymentServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private MockPaymentService paymentService;

    private Booking booking;

    @BeforeEach
    void setup() {
        booking = new Booking();
        booking.setId(1L);
        booking.setStatus(Status.PENDING);

        // inject fake secret key (since @Value is not loaded in unit tests)
        ReflectionTestUtils.setField(paymentService, "secretKey", "test_secret_key");
    }


    @Test
    void createPaymentIntent_successfulPayment_setsStatusPending() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        PaymentIntentResponse response =
                paymentService.createPaymentIntent(1L, 100.0);

        assertEquals(Status.PENDING, response.status());
        assertEquals("MOCK_PI_1", response.paymentId());
        assertEquals("MOCK_CLIENT_SECRET_1", response.clientSecret());

        assertEquals(Status.PENDING, booking.getStatus());
        verify(bookingRepository).save(booking);
    }

    @Test
    void createPaymentIntent_failedPayment_setsStatusCancelled() {
        // force failure
        ReflectionTestUtils.setField(paymentService,
                "SimulateSuccessfulPayment", false);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        PaymentIntentResponse response =
                paymentService.createPaymentIntent(1L, 100.0);

        assertEquals(Status.CANCELLED, response.status());
        assertEquals(Status.CANCELLED, booking.getStatus());

        verify(bookingRepository).save(booking);
    }

    @Test
    void createPaymentIntent_bookingNotFound_throwsException() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> paymentService.createPaymentIntent(1L, 100.0)
        );

        assertEquals("Booking Not Found", ex.getMessage());
        verify(bookingRepository, never()).save(any());
    }



    @Test
    void confirmPayment_success_setsStatusConfirmed() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        PaymentConfirmationResponse response =
                paymentService.confirmPayment(1L, "MOCK_PI_1");

        assertEquals(Status.CONFIRMED, response.status());
        assertEquals("MOCK_PI_1", response.Payment_ID());

        assertEquals(Status.CONFIRMED, booking.getStatus());
    }

    @Test
    void confirmPayment_bookingNotFound_throwsException() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> paymentService.confirmPayment(1L, "MOCK_PI_1")
        );

        assertEquals("Booking Not Found", ex.getMessage());
    }
}
