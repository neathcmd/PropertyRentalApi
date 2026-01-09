package com.rental.PropertyRentalApi.Service;

import com.rental.PropertyRentalApi.DTO.request.ReviewRequest;
import org.springframework.stereotype.Service;

@Service
public interface ReviewService {

    void createReview(ReviewRequest request, Long userId);

    Double getAverageRating(Long propertyId);
}