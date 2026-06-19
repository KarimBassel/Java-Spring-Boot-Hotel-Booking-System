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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private HotelRepository hotelRepository;

    private User user;
    private Hotel hotel;

    @BeforeEach
    void setup() {
        user = new User();
        user.setId(1L);
        user.setName("Karim");

        hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Hilton");
    }

    // ================= CREATE REVIEW =================
    @Test
    void shouldCreateReviewSuccessfully() {

        CreateReviewRequest request =
                new CreateReviewRequest(1L, 5.0, "Great");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(reviewRepository.existsByUserAndHotel(user, hotel)).thenReturn(false);

        Review saved = new Review();
        saved.setId(10L);
        saved.setUser(user);
        saved.setHotel(hotel);
        saved.setReview(5.0);
        saved.setComment("Great");

        when(reviewRepository.save(any())).thenReturn(saved);

        ReviewResponse response =
                reviewService.addReview(1L, request);

        assertEquals(10L, response.id());
        assertEquals(5.0, response.review());
    }

    // ================= GET PAST REVIEW =================
    @Test
    void shouldReturnPastReview() {

        Review review = new Review();
        review.setId(1L);
        review.setUser(user);
        review.setHotel(hotel);
        review.setReview(4.0);
        review.setComment("Nice");

        when(reviewRepository.getUserHotelReview(1L, 1L))
                .thenReturn(review);

        ReviewResponse response =
                reviewService.getPastReview(1L, 1L);

        assertEquals(4.0, response.review());
    }

    @Test
    void shouldThrowWhenReviewNotFound() {

        when(reviewRepository.getUserHotelReview(1L, 1L))
                .thenReturn(null);

        ReviewResponse reviewnotfound = reviewService.getPastReview(1L,1L);
        assertEquals(-1L , reviewnotfound.id());
    }

    @Test
    void shouldUpdateReview() {

        Review review = new Review();
        review.setId(1L);
        review.setReview(3.0);
        review.setComment("old");

        review.setUser(user);
        review.setHotel(hotel);

        when(reviewRepository.findById(1L))
                .thenReturn(Optional.of(review));

        when(reviewRepository.save(any())).thenReturn(review);

        UpdateReviewRequest request =
                new UpdateReviewRequest(1L, 5.0, "new");

        ReviewResponse response =
                reviewService.updateReview(request);

        assertEquals(5.0, response.review());
        assertEquals("new", response.comment());
    }

    @Test
    void shouldDeleteReview() {

        doNothing().when(reviewRepository).deleteById(1L);

        reviewService.deleteReview(1L);

        verify(reviewRepository, times(1))
                .deleteById(1L);
    }

    @Test
    void shouldGetReviewsByHotel() {

        Review review = new Review();
        review.setUser(user);
        review.setHotel(hotel);
        review.setReview(4.0);

        when(hotelRepository.findById(1L))
                .thenReturn(Optional.of(hotel));

        when(reviewRepository.findByHotelOrderByIdDesc(hotel))
                .thenReturn(List.of(review));

        List<ReviewResponse> result =
                reviewService.getByHotel(1L);

        assertEquals(1, result.size());
    }
}