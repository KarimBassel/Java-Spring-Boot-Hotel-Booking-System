package com.hotel.booking.service;

import com.hotel.booking.model.Hotel;
import com.hotel.booking.model.Review;
import com.hotel.booking.model.User;
import com.hotel.booking.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;


    public Review addReview(User user, Hotel hotel, double rating) {
        Review review = new Review(user, hotel, rating);
        return reviewRepository.save(review);
    }


    public Optional<Review> updateReview(Long reviewId, double newRating) {
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        if (optionalReview.isPresent()) {
            Review review = optionalReview.get();
            review.setReview(newRating);
            reviewRepository.save(review);
        }
        return optionalReview;
    }


    public boolean deleteReview(Long reviewId) {
        if (reviewRepository.existsById(reviewId)) {
            reviewRepository.deleteById(reviewId);
            return true;
        }
        return false;
    }


    public List<Review> getReviewsByHotel(Hotel hotel) {
        return reviewRepository.findByHotel(hotel);
    }


    public List<Review> getReviewsByUser(User user) {
        return reviewRepository.findByUser(user);
    }

    public double getAverageRating(Hotel hotel) {
        List<Review> reviews = reviewRepository.findByHotel(hotel);
        return reviews.stream()
                .mapToDouble(Review::getReview)
                .average()
                .orElse(0.0);
    }
}
