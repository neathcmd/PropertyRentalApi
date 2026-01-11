package com.rental.PropertyRentalApi.Service;

import com.rental.PropertyRentalApi.DTO.request.ReviewCreateRequest;
import com.rental.PropertyRentalApi.DTO.request.ReviewUpdateRequest;
import com.rental.PropertyRentalApi.DTO.response.ReviewResponse;
import org.springframework.data.domain.Page;

public interface ReviewService {

    // Create a review for a property
    ReviewResponse createReview(Long propertyId, ReviewCreateRequest request);

    // Update user's own review
    ReviewResponse updateReview(Long reviewId, ReviewUpdateRequest request);

    // Delete user's own review
    void deleteReview(Long reviewId);

    // Get all reviews for a property (paginated)
    Page<ReviewResponse> getReviewsByProperty(Long propertyId, int page, int size);

    // Get current user's reviews (paginated)
    Page<ReviewResponse> getCurrentUserReviews(int page, int size);

    // Get a specific review by ID
    ReviewResponse getReviewById(Long reviewId);
}