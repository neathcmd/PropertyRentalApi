//package com.rental.PropertyRentalApi.Service.Impl;
//
//
//import com.rental.PropertyRentalApi.DTO.request.ReviewRequest;
//import com.rental.PropertyRentalApi.Entity.PropertyEntity;
//import com.rental.PropertyRentalApi.Entity.ReviewEntity;
//import com.rental.PropertyRentalApi.Entity.UserEntity;
//import com.rental.PropertyRentalApi.Repository.ReviewRepository;
//import com.rental.PropertyRentalApi.Repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class ReviewServiceImpl {
//    private final ReviewRepository reviewRepository;
//    private final PropertyRepository propertyRepository;
//    private final UserRepository userRepository;
//
//    @Override
//    public ReviewEntity createReview(ReviewRequest request) {
//
//        // Validate rating range
//        if (request.getRating() < 1 || request.getRating() > 5) {
//            throw new RuntimeException("Rating must be between 1 and 5");
//        }
//
//        // Prevent duplicate review
//        if (reviewRepository.existsByUserIdAndPropertyId(
//                request.getUserId(), request.getPropertyId())) {
//            throw new RuntimeException("You already reviewed this property");
//        }
//
//        PropertyEntity property = propertyRepository.findById(request.getPropertyId())
//                .orElseThrow(() -> new RuntimeException("Property not found"));
//
//        UserEntity user = userRepository.findById(request.getUserId())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        ReviewEntity review = ReviewEntity.builder()
//                .rating(request.getRating())
//                .comment(request.getComment())
//                .property(property)
//                .user(user)
//                .createdAt(LocalDateTime.now())
//                .build();
//
//        return reviewRepository.save(review);
//    }
//
//    @Override
//    public List<ReviewEntity> getReviewsByProperty(Long propertyId) {
//        return reviewRepository.findByPropertyId(propertyId);
//    }
//}
