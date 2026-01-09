package com.rental.PropertyRentalApi.Controller;

import com.rental.PropertyRentalApi.DTO.request.ReviewRequest;
import com.rental.PropertyRentalApi.Entity.UserEntity;
import com.rental.PropertyRentalApi.Service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<?> createReview(
            @RequestBody @Valid ReviewRequest request,
            @AuthenticationPrincipal UserEntity user
    ) {
        reviewService.createReview(request, user.getId());
        return ResponseEntity.ok("Review created successfully");
    }

    @GetMapping("/average/{propertyId}")
    public ResponseEntity<Double> getAverageRating(
            @PathVariable Long propertyId
    ) {
        return ResponseEntity.ok(reviewService.getAverageRating(propertyId));
    }
}
