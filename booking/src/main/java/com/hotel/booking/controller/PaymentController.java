package com.hotel.booking.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.*;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import java.util.HashMap;
import java.util.Map;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    // Accept amount in the request body as JSON
    @PostMapping("/create")
    public Map<String, String> createPaymentIntent(@RequestBody Map<String, Long> payload) throws Exception {

        Long amount = payload.get("amount"); // extract amount from JSON
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("Amount must be a positive number");
        }

        // Create Stripe PaymentIntent
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amount * 100) // Stripe expects cents
                .setCurrency("usd")
                .build();

        PaymentIntent intent = PaymentIntent.create(params);

        Map<String, String> response = new HashMap<>();
        response.put("clientSecret", intent.getClientSecret());

        return response;
    }
}