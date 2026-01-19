package com.hotel.booking.service;

import com.hotel.booking.model.Hotel;
import com.hotel.booking.model.Review;
import com.hotel.booking.model.User;
import com.hotel.booking.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewrepository;

    @InjectMocks
    private ReviewService reviewService;

    private User user;
    private Hotel hotel;
    private Review review;

    @BeforeEach
    void setup() {
        user = new User();
        user.setId(1L);

        hotel = new Hotel();
        hotel.setId(1L);

        review = new Review(user, hotel, 4.0);
        review.setId(1L);
    }


    @Test
    void addReview_savesAndReturnsReview() {
        when(reviewrepository.save(any(Review.class))).thenReturn(review);

        Review result = reviewService.addReview(user, hotel, 4.0);

        assertNotNull(result);
        assertEquals(4.0, result.getReview());
        assertEquals(user, result.getUser());
        assertEquals(hotel, result.getHotel());

        verify(reviewrepository).save(any(Review.class));
    }

    @Test
    void updateReview_whenReviewExists_updatesRating() {
        when(reviewrepository.findById(1L)).thenReturn(Optional.of(review));
        when(reviewrepository.save(review)).thenReturn(review);

        Optional<Review> result =
                reviewService.updateReview(1L, 5.0);

        assertTrue(result.isPresent());
        assertEquals(5.0, result.get().getReview());

        verify(reviewrepository).save(review);
    }

    @Test
    void updateReview_whenReviewNotFound_returnsEmptyOptional() {
        when(reviewrepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Review> result =
                reviewService.updateReview(1L, 5.0);

        assertFalse(result.isPresent());
        verify(reviewrepository, never()).save(any());
    }


    @Test
    void deleteReview_whenExists_deletesAndReturnsTrue() {
        when(reviewrepository.existsById(1L)).thenReturn(true);

        boolean result = reviewService.deleteReview(1L);

        assertTrue(result);
        verify(reviewrepository).deleteById(1L);
    }

    @Test
    void deleteReview_whenNotExists_returnsFalse() {
        when(reviewrepository.existsById(1L)).thenReturn(false);

        boolean result = reviewService.deleteReview(1L);

        assertFalse(result);
        verify(reviewrepository, never()).deleteById(anyLong());
    }


    @Test
    void getReviewsByHotel_returnsList() {
        when(reviewrepository.findByHotel(hotel))
                .thenReturn(List.of(review));

        List<Review> reviews =
                reviewService.getReviewsByHotel(hotel);

        assertEquals(1, reviews.size());
        verify(reviewrepository).findByHotel(hotel);
    }

    @Test
    void getReviewsByUser_returnsList() {
        when(reviewrepository.findByUser(user))
                .thenReturn(List.of(review));

        List<Review> reviews =
                reviewService.getReviewsByUser(user);

        assertEquals(1, reviews.size());
        verify(reviewrepository).findByUser(user);
    }


    @Test
    void getAverageRating_returnsCorrectAverage() {
        Review r1 = new Review(user, hotel, 4.0);
        Review r2 = new Review(user, hotel, 2.0);

        when(reviewrepository.findByHotel(hotel))
                .thenReturn(List.of(r1, r2));

        double avg = reviewService.getAverageRating(hotel);

        assertEquals(3.0, avg);
    }

    @Test
    void getAverageRating_noReviews_returnsZero() {
        when(reviewrepository.findByHotel(hotel))
                .thenReturn(List.of());

        double avg = reviewService.getAverageRating(hotel);

        assertEquals(0.0, avg);
    }


}
