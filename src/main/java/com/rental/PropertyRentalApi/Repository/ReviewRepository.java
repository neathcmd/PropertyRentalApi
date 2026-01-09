package com.rental.PropertyRentalApi.Repository;

import com.rental.PropertyRentalApi.Entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    boolean existsByProperty_IdAndUser_Id(Long propertyId, Long userId);

    @Query("SELECT AVG(r.rating) FROM ReviewEntity r WHERE r.property.id = :propertyId")
    Double findAverageRating(@Param("propertyId") Long propertyId);
}
