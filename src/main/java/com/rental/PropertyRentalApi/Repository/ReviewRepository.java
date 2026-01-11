package com.rental.PropertyRentalApi.Repository;

import com.rental.PropertyRentalApi.Entity.PropertyEntity;
import com.rental.PropertyRentalApi.Entity.ReviewEntity;
import com.rental.PropertyRentalApi.Entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    // Check if user already reviewed this property
    boolean existsByUserAndProperty(UserEntity user, PropertyEntity property);

    // Find a specific user's review for a property
    Optional<ReviewEntity> findByUserAndProperty(UserEntity user, PropertyEntity property);

    // Get all reviews for a property (paginated)
    Page<ReviewEntity> findAllByProperty(PropertyEntity property, Pageable pageable);

    // Get all reviews by a user (paginated)
    Page<ReviewEntity> findAllByUser(UserEntity user, Pageable pageable);

    // Count reviews for a property
    long countByProperty(PropertyEntity property);
}