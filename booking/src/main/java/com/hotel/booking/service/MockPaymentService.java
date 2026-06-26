package com.hotel.booking.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.hotel.booking.dto.PaymentIntentResponse;
import com.hotel.booking.dto.PaymentConfirmationResponse;
import com.hotel.booking.model.Enums.Status;
import com.hotel.booking.repository.BookingRepository;
import com.hotel.booking.model.Booking;


/**
 * Simulating Stripe Payment Workflow
 * 1- Storing a fake secret key as an env var (simulating stripe secret key)
 * 2- saving the secret key through application.props
 * 3- on Frontend calling CreatePayment, Backend Simulates creating a payment intent using the secret key
 * 4- Backend Responds to Frontend with the client secret
 * 5-  To simulate payment, the frontend uses:
 *  *      - the client_secret
 *  *      - fake credit card data (e.g., test MasterCard number)
 *  *    and then calls the backend's /confirm-payment endpoint.
 * 6- front end simulates confirmation by calling confirmPayment from backend
 * 7- backend updates booking as paid and return success dto to frontend
 *
 *  * This flow mirrors the real Stripe lifecycle:
 *  *    Booking → PaymentIntent → ClientSecret → PaymentConfirmation → StatusUpdated
 *
 */
// will be changed to @Profile('prod') in case of production ready version

@Service
public class MockPaymentService implements PaymentServicee {

    @Autowired
    private BookingRepository bookingRepository;

    @Value("${stripe.secret-key}")
    private String secretKey; // just to simulate usage

    boolean SimulateSuccessfulPayment=true;
    @Override
    public PaymentIntentResponse createPaymentIntent(Long bookingId, double amount) {
        Booking obj = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking Not Found"));

        // simulate Stripe generating a PaymentIntent using the secret key found in the env vars
        String paymentId = "MOCK_PI_" + bookingId;
        String clientSecret = "MOCK_CLIENT_SECRET_" + bookingId;

        System.out.println("Using secret key: " + secretKey); // just to simulate


        if(SimulateSuccessfulPayment){
            //update booking object with payment pending until confirmation called from frontend
            obj.setStatus(Status.PENDING);
            bookingRepository.save(obj);


            return new PaymentIntentResponse(paymentId, clientSecret, Status.PENDING);
        }
        else{
            //update booking object with payment failure
            obj.setStatus(Status.CANCELLED);
            bookingRepository.save(obj);

            return new PaymentIntentResponse(paymentId, clientSecret, Status.CANCELLED);
        }

    }
    //should confirm payment in the frontend using stripe.js
    //then stripe notifies the backend using a Web Hook
    //this function simulates this scenario
    //to simulate the process, frontend with call both functions
    @Override
    public PaymentConfirmationResponse confirmPayment(Long BookingID, String paymentId) {
        Booking obj = bookingRepository.findById(BookingID)
                .orElseThrow(() -> new RuntimeException("Booking Not Found"));
        obj.setStatus(Status.CONFIRMED);

        // confirmation called from frontend
        return new PaymentConfirmationResponse("MOCK_PI_"+BookingID, Status.CONFIRMED);
    }
}

