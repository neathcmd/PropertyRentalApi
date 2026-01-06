//package com.rental.PropertyRentalApi.Repository;
//
//import com.rental.PropertyRentalApi.Entity.ReviewEntity;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.List;
//import java.util.Optional;
//
//public interface ReviewRepository extends JpaRepository<ReviewEntity,Long> {
//    boolean existsByUserIdAndPropertyId(Long userId, Long propertyId);
//
//    List<ReviewEntity> findByPropertyId(Long propertyId);
//
//    Optional<ReviewEntity> findByUserIdAndPropertyId(Long userId, Long propertyId);
//}
