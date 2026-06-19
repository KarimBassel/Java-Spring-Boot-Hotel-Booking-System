package com.hotel.booking.controller;

import com.hotel.booking.dto.CreateReviewRequest;
import com.hotel.booking.dto.ReviewResponse;
import com.hotel.booking.dto.UpdateReviewRequest;
import com.hotel.booking.service.CurrentUserService;
import com.hotel.booking.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private CurrentUserService currentUserService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<ReviewResponse>> getAllReviews(){
        return ResponseEntity.ok(
                reviewService.getAllReviews()
        );
    }
    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(
            @RequestBody CreateReviewRequest request) {
        return ResponseEntity.ok(
                reviewService.addReview(currentUserService.getCurrentUserId(), request)
        );
    }

    @GetMapping("/user-review")
    public ResponseEntity<ReviewResponse> getPastReview(@RequestParam Long hotelID){

        ReviewResponse reviewResponse = reviewService.getPastReview(currentUserService.getCurrentUserId(), hotelID);

        if(reviewResponse.id() == -1){
            return  ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(
                reviewService.getPastReview(currentUserService.getCurrentUserId(), hotelID)
        );
    }


    @PutMapping
    public ResponseEntity<ReviewResponse> updateReview(
            @RequestBody UpdateReviewRequest updateReviewRequest
    ) {
        return ResponseEntity.ok(
                reviewService.updateReview(updateReviewRequest)
        );
    }


    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<ReviewResponse>> getHotelReviews(
            @PathVariable Long hotelId
    ) {
        return ResponseEntity.ok(
                reviewService.getByHotel(hotelId)
        );
    }

    //returns reviews made by the currently authenticated user
    @GetMapping("/me")
    public ResponseEntity<List<ReviewResponse>> getMyReviews() {
        return ResponseEntity.ok(
                reviewService.getByUser(currentUserService.getCurrentUserId())
        );
    }

}