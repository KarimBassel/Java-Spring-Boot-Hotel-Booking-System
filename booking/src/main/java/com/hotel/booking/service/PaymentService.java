package com.hotel.booking.service;
import com.hotel.booking.exception.ResourceNotFoundException;
import com.hotel.booking.model.Booking;
import com.hotel.booking.model.Enums.PaymentMethod;
import com.hotel.booking.model.Enums.Status;
import com.hotel.booking.model.Payment;
import com.hotel.booking.repository.BookingRepository;
import com.hotel.booking.repository.PaymentRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.hotel.booking.dto.PaymentIntentResponse;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private PaymentIntentCreateParams params;


    @Transactional
    public PaymentIntentResponse createPaymentIntent(Long bookingId, double amount) throws StripeException {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking" , bookingId));

        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount((long) (amount * 100)) 
                        .setCurrency("usd")
                        .putMetadata("bookingId", bookingId.toString())
                        .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);

        booking.setStatus(Status.PENDING);
        bookingRepository.save(booking);

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setPaymentIntentID(paymentIntent.getId());
        payment.setAmountToBePaid(amount);
        payment.setPaymentStatus(Status.PENDING);
        payment.setPaymentMethod(PaymentMethod.STRIPE);

        paymentRepository.save(payment);

        return new PaymentIntentResponse(
                paymentIntent.getId(),
                paymentIntent.getClientSecret(),
                Status.PENDING
        );
    }
    
    @Transactional
    public void handleSuccessfulPayment(String paymentIntentId,Long bookingID) {

        Payment payment =
                paymentRepository.findBypaymentIntentId(
                        paymentIntentId
                );

        Booking booking = bookingRepository.findById(bookingID)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Booking", bookingID));

        if (payment == null) {
            throw new ResourceNotFoundException("Payment",paymentIntentId);
        }

        payment.setPaymentStatus(Status.CONFIRMED);
        payment.setPaymentDate(LocalDate.now());

        booking.setStatus(Status.CONFIRMED)
        ;
        paymentRepository.save(payment);
        bookingRepository.save(booking);
    }
}
