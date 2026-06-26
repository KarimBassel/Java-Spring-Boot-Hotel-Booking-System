package com.hotel.booking.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.hotel.booking.dto.CreatePaymentRequest;
import com.hotel.booking.dto.PaymentIntentResponse;
import com.hotel.booking.service.PaymentService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.net.Webhook;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    @PostMapping("/create")
    public ResponseEntity<PaymentIntentResponse> createPaymentIntent(
            @RequestBody CreatePaymentRequest request
    ) throws StripeException {

        PaymentIntentResponse response =
                paymentService.createPaymentIntent(
                        request.BookingID(),
                        request.Amount()
                );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/webhooks/stripe")
    public ResponseEntity<String> confirmPayment(
            HttpServletRequest request
    ) {

        try {

            String payload = new String(request.getInputStream().readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);

            String sigHeader =
                    request.getHeader("Stripe-Signature");

            Event event = Webhook.constructEvent(
                    payload,
                    sigHeader,
                    endpointSecret
            );
            //for debugging
            System.out.println("EVENT RECEIVED: " + event.getType());

            switch (event.getType()) {

                case "payment_intent.succeeded" -> {

                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode root = mapper.readTree(event.toJson());
                    //Extract PaymentintentID and BookingID from JSON
                    String paymentIntentId =
                            root.get("data")
                                    .get("object")
                                    .get("id")
                                    .asText();

                    PaymentIntent paymentIntent =
                            PaymentIntent.retrieve(paymentIntentId);

                    String bookingIdStr =
                            paymentIntent.getMetadata().get("bookingId");

                    Long bookingId = Long.parseLong(bookingIdStr);

                    paymentService.handleSuccessfulPayment(
                            paymentIntentId,
                            bookingId
                    );
                }

                case "payment_intent.payment_failed" -> {
                    System.out.println(
                            "Payment failed webhook received"
                    );
                }

                default -> {
                    System.out.println(
                            "Unhandled event type: "
                                    + event.getType()
                    );
                }
            }

            return ResponseEntity.ok("Webhook processed");

        } catch (SignatureVerificationException e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid signature");

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity.status(
                    HttpStatus.INTERNAL_SERVER_ERROR
            ).body("Webhook failed");
        }
    }
}