package com.hotel.booking.service;

import com.hotel.booking.dto.CreateReviewRequest;
import com.hotel.booking.dto.ReviewResponse;
import com.hotel.booking.dto.UpdateReviewRequest;
import com.hotel.booking.model.Hotel;
import com.hotel.booking.model.Review;
import com.hotel.booking.model.User;
import com.hotel.booking.repository.HotelRepository;
import com.hotel.booking.repository.ReviewRepository;
import com.hotel.booking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HotelRepository hotelRepository;


    public ReviewResponse addReview(Long userID, CreateReviewRequest request) {

        User user = userRepository.findById(userID)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Hotel hotel = hotelRepository.findById(request.hotelID())
                .orElseThrow(() -> new RuntimeException("Hotel not found"));

        if (reviewRepository.existsByUserAndHotel(user, hotel)) {
            throw new RuntimeException("You already reviewed this hotel");
        }

        Review review = new Review();
        review.setUser(user);
        review.setHotel(hotel);
        review.setReview(request.rating());
        review.setComment(request.comment());
        review.setCreatedAt(LocalDate.now());

        Review saved = reviewRepository.save(review);

        return mapToResponse(saved);
    }
    public ReviewResponse getPastReview(Long userID, Long hotelID){

        Review review =
                reviewRepository.getUserHotelReview(userID, hotelID);

        if(review == null){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Review not found"
            );
        }

        return mapToResponse(review);
    }

    public ReviewResponse updateReview(UpdateReviewRequest updateReviewRequest) {

        Review review = reviewRepository.findById(updateReviewRequest.reviewID())
                .orElseThrow(() -> new RuntimeException("Review not found"));

        review.setReview(updateReviewRequest.rating());
        review.setComment(updateReviewRequest.comment());


        return mapToResponse(reviewRepository.save(review));
    }

    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    public List<ReviewResponse> getByHotel(Long hotelId) {

        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new RuntimeException("Hotel not found"));

        return reviewRepository.findByHotelOrderByIdDesc(hotel)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<ReviewResponse> getByUser(Long userID) {

        User user = userRepository.findById(userID)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return reviewRepository.findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public double getAverageRating(Long hotelId) {

        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new RuntimeException("Hotel not found"));

        return reviewRepository.findByHotel(hotel)
                .stream()
                .mapToDouble(Review::getReview)
                .average()
                .orElse(0.0);
    }

    private ReviewResponse mapToResponse(Review review) {

        return new ReviewResponse(
                review.getId(),
                review.getUser().getId(),
                review.getUser().getName(),
                review.getHotel().getId(),
                review.getHotel().getName(),
                review.getReview(),
                review.getComment(),
                review.getCreatedAt()
        );
    }
}